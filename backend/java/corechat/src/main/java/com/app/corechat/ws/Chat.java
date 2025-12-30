package com.app.corechat.ws;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.app.corechat.business.chat.MessageService;

import jakarta.ejb.EJB;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat/{conversationId}")
public class Chat {

    private static final Logger LOG = Logger.getLogger(Chat.class.getName());

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @EJB
    private MessageService messageService;



    @OnOpen
    public void onOpen(Session session, @PathParam("conversationId") Long conversationId) {
        session.getUserProperties().put("conversationId", conversationId);
        sessions.add(session);
        LOG.info("🔵 WS OPEN - Session ID: " + session.getId()+" total sessions: "+sessions.size());


    }

    @OnMessage
    public void onMessage(String message, Session session) {

        Long conversationId = (Long) session.getUserProperties().get("conversationId");


        LOG.info("📨 WS MESSAGE - Session: " + session.getId() + ", Message: " + message);

        Principal p = session.getUserPrincipal();
        String email = p.getName();
        
        if (email != null) {
            messageService.sendMessage(conversationId, email,message);
        }

        // synchronized(sessions){
            
        //     for (Session s : sessions){
        //         if(s.isOpen()){
                    
        //             s.getAsyncRemote().sendText(message);
        //         }
        //     }
        // }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        LOG.info("🔴 WS CLOSED - Session ID: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.severe("❌ WS ERROR - Session: " + session.getId() + ", Error: " + error.getMessage());
    }
}
