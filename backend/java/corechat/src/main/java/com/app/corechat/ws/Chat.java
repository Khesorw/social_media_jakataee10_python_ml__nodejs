package com.app.corechat.ws;

import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.MessageDTO;
import com.app.corechat.business.chat.MessageService;
import com.app.corechat.business.chat.ParticipantConversationFacade;
import com.app.corechat.business.user.UserFecade;
import com.app.corechat.entities.Message;
import com.app.corechat.filter.WebSocketConfigurator;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{chatId}",
        configurator = WebSocketConfigurator.class
            
)
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
    public void onOpen(Session session, @PathParam("chatId") Long conversationId) throws IOException {
        

        if (conversationId == null) {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "null conversation id"));
            return;
        } //end if
        
        LOG.info("conversation id from onOpen: " + conversationId);
        try {

            Principal p = session.getUserPrincipal();
            if (p == null) {
                try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "no principal or not authenticated"));  

                } catch (IOException e) {
                    
                }//end try
                return;
            } //end if
            

            //Add new session in the sessionByConvId whenever there is a new connection
            String email = session.getUserPrincipal().getName();
            Long userId = userFecade.findUserIdByEmail(email);
            LOG.info("Got id "+userId+" for this principal: "+email);
            if (participantConversationFacade.isParticipant(conversationId, userId)) {

                //later to be used onsend message
                session.getUserProperties().put("chatId", conversationId);

                //add new session
                sessionByConvId.computeIfAbsent(conversationId, k -> ConcurrentHashMap.newKeySet()).add(session);
            } else {

                session.close(new CloseReason(
                        CloseReason.CloseCodes.VIOLATED_POLICY,
                        "Not a user"));
            }
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }//end try
        
        LOG.info("🔵 WS OPEN - Session ID: "+session.getId()+" for conversation: "+conversationId );

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        Long conversationId = (Long) session.getUserProperties().get("chatId");
        if (conversationId == null) {
            throw new IllegalArgumentException("⛔ Null conversationID in onMessage");
        }

        LOG.info("📨 WS MESSAGE - Session: " + session.getId() + ", Message: " + message);

        try {
            // JsonObject json = Json.createReader(new StringReader(message)).readObject();
            actionOnMessage(message, session, conversationId);
            

        } catch (Exception e) {
            LOG.warning("Failed to parse Message");
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


    /**
     * 
     * @param message
     * @param session
     * @param conversationId
     */
    private void actionOnMessage(String message, Session session, Long conversationId) {

        JsonObject msg = Json.createReader(new StringReader(message)).readObject();

        String type = msg.getString("type", "");//extracting the type
        // String textM = msg.getJsonObject("payload").getString("text","notAType");
        // if (textM == null) {
        //     throw new JsonException("Could not get text from json object: ");
        // }
        LOG.info("printing type from jsonObject" + type + " now priniting actual text: ");

        switch (type) {
            case "chat_message":
                String email = session.getUserPrincipal().getName();
                String textM = msg.getJsonObject("payload").getString("text", "notAType");
                if (textM == null) {
                    throw new JsonException("Could not get text from json object: ");
                }
                handleMessage(session, email, conversationId, textM);
                break;

            case "call_intent":
            case "call_response":
            case "call_offer":
            case "call_answer":
            case "ice_candidate":
            case "call_end":
                handleCall(session, conversationId, message);
                break;

            default:
                LOG.warning("wrong or unknown type " + type);
        }

    }
    

    /**
     * Handle Text (Chat) MESSAGEs it persist then route the message to the conversation participant
     * @param session
     * @param email
     * @param conversationId
     * @param textM
     */

    public void handleMessage(Session session, String email, Long conversationId, String textM) {
        /*route the message */      
          Message saved;
                if (email != null) {
                    //persist the message
                    saved = messageService.sendMessage(conversationId, email, textM);
                } else {
                    return;
                }
           MessageDTO dto = new MessageDTO();
           dto.id = saved.getId();
           dto.text = saved.getMessageText();
           dto.createdAt = saved.getCreatedAt();
           dto.senderId = saved.getSender().getId();

           String jsonStr = JsonbBuilder.create().toJson(dto);
           
           JsonObject jsonObject = Json.createReader(new StringReader(jsonStr)).readObject();
           JsonObject updaObject = Json.createObjectBuilder(jsonObject)
                   .add("type", "chat_message").build();
                                
           String json = updaObject.toString();
           

            LOG.info(() -> "MEssage to send to front end: "+json);

            //sends message to the participant of conversation room
            Set<Session> sessions = sessionByConvId.get(conversationId);
            if (sessions != null) {

                for (Session s : sessions) {
                    if ( s.isOpen() && (!s.equals(session))) {
                        s.getAsyncRemote().sendText(json);
                    }
                }
            }
        }//handleMessage()
    





    /**
     * Handle all call MESSAGEs
     * @param session
     * @param conversationId
     * @param msg
     */

    public void handleCall(Session session, Long conversationId, String msg) {

          LOG.info(()->"HandleCall MEssage to send to front end: "+msg);

            //sends message to the participant of conversation room
            Set<Session> sessions = sessionByConvId.get(conversationId);
            if (sessions != null) {
                for (Session s : sessions) {
                    if (s.isOpen() && (!s.equals(session))) {
                        s.getAsyncRemote().sendText(msg);
                    }
                }
            }
              
    }//handleCall()


}
