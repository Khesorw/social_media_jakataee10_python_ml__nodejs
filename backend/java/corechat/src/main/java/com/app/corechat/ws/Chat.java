package com.app.corechat.ws;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.app.corechat.business.chat.MessageService;
import com.app.corechat.business.chat.ParticipantConversationFacade;
import com.app.corechat.business.user.UserFecade;

import jakarta.ejb.EJB;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{chatId}")
public class Chat {

    private static final Logger LOG = Logger.getLogger(Chat.class.getName());
    private static final Map<Long, Set<Session>> sessionByConvId = new ConcurrentHashMap<>();
    
    

    @EJB
    private MessageService messageService;

    @EJB
    private ParticipantConversationFacade participantConversationFacade;

    @EJB
    private UserFecade userFecade;



    @OnOpen
    public void onOpen(Session session, @PathParam("chatId") Long conversationId) {
        
        try {

            Principal p = session.getUserPrincipal();
            if (p == null) {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "no principal or not authenticated"));
                return;
            }
            String email = session.getUserPrincipal().getName();
            Long userId = userFecade.findUserIdByEmail(email);
            if (participantConversationFacade.isParticipant(conversationId, userId)) {

                session.getUserProperties().put("chatId", conversationId);
                //add new session
                sessionByConvId
                        .computeIfAbsent(conversationId, id -> ConcurrentHashMap.newKeySet()).add(session);
            } else {

                session.close(new CloseReason(
                        CloseReason.CloseCodes.VIOLATED_POLICY,
                        "Now a user"));
            }
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        
        LOG.info("🔵 WS OPEN - Session ID: "+session.getId()+" for conversation: "+conversationId );

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        Long conversationId = (Long) session.getUserProperties().get("chatId");
        if(conversationId == null){return;}
        LOG.info("📨 WS MESSAGE - Session: " + session.getId() + ", Message: " + message);
        Principal p = session.getUserPrincipal();
        String email = p.getName();
        if (email != null) {
            //persist the message
            messageService.sendMessage(conversationId, email, message);
        }
        
        //sends message to the participant of conversation room
        Set<Session> sessions = sessionByConvId.get(conversationId);
        if (sessions != null) {
            
            for (Session s : sessions) {
                if (! s.equals(session) && s.isOpen()) {
                    s.getAsyncRemote().sendText(message);
                }
            }
        }

    }
    


    @OnClose
    public void onClose(Session session) {
        
        Long conversationId = (Long) session.getUserProperties().get("chatId");
        
        Set<Session> s = sessionByConvId.get(conversationId);
        if (s != null) {
            s.remove(session);
            if (s.isEmpty()) {
                sessionByConvId.remove(conversationId);
                
            }
        }

        LOG.info("🔴 WS CLOSED - Session ID: " + session.getId());
    }
    


    @OnError
    public void onError(Session session, Throwable error) {
        LOG.severe("❌ WS ERROR - Session: " + session.getId() + ", Error: " + error.getMessage());
    }
}
