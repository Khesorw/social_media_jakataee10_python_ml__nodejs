package com.app.corechat.business.chat;

import java.util.List;

import com.app.corechat.business.user.UserFecade;
import com.app.corechat.entities.Conversation;
import com.app.corechat.entities.Message;
import com.app.corechat.entities.User;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.enterprise.SecurityContext;


@Stateless
public class MessageService {


    @EJB
   private ConversationFacade conversationFacade;

    @EJB
    private ParticipantConversationFacade participantConversationFacade;

    @EJB
    private UserFecade userFecade;

    @EJB
    private MessageFacade messageFacade;

    @Inject
    private SecurityContext securityContext;



    



    /**
     * persist each message upon creation
     * @param conversationId
     * @param txt
     */
    public void sendMessage(Long conversationId, String eml, String txt) {

        //cheap validation 
        if (conversationId == null) {
            throw new IllegalArgumentException("conversationid is null");

        }
        
         //strip and checks if msg is not blank or null
        String email = checkAndValidateText(eml);
     
        String msg = checkAndValidateText(txt); 
    
        Long userId = userFecade.findUserIdByEmail(email);
        if (userId == null) {
            throw new SecurityException("user is not registered or authenticated");
        }

        //checks and validate that conversation exist
        if (!(conversationFacade.doesExist(conversationId))) {
            throw new EntityNotFoundException("conversation " + conversationId + " does not exist");
        }

        //checks if sender is a participant for x conversation
        if (!participantConversationFacade.isParticipant(conversationId, userId)) {
            throw new SecurityException("sender is not a participant of {" + conversationId + "} conversaiond");
        }

        //creates new conversation and for the given convid to persist once user is set
        Conversation c = conversationFacade.find(conversationId);
        User u = userFecade.findById(userId);
        Message message = new Message();
        message.setConversation(c);
        message.setSender(u);
        message.setMessageText(msg);
        messageFacade.saveMessage(message);
    }
    






    /**
     * Get messages for the x conversation. Validate Input, validate conversation exists,
             * validate authorization, talk to facade
     * @param email
     * @param conversationId
     * @param limit
     * @param offset
     * @return List of Messages for the specified conversation-room and authorized user
     */
    public List<Message> getMessages(Long conversationId, int limit, int offset) {

        //cheap validation 
        if (conversationId == null) {
            throw new IllegalArgumentException("conversationid is null");
        }

        //checks the pagination (offset and limit) for illegal argument
        if (limit <= 0 || limit > 100) {
            throw new IllegalArgumentException("Invalide limit");
        }
        
        if (offset < 0) {
            throw new IllegalArgumentException("Invalide offset");

        }
        
        //checks and validate that conversation exist
        if (!(conversationFacade.doesExist(conversationId))) {
            throw new EntityNotFoundException("conversation " + conversationId + " does not exist");
        }


        //strip email and check for null or empty
        String email = securityContext.getCallerPrincipal().getName();
        if (email == null) {
            throw new SecurityException("un-authenticated user");
        }

        //gets userid form User through email
        Long userId = userFecade.findUserIdByEmail(email);
        if (userId == null) {
            throw new SecurityException("user is not registered or authenticated");
        }

        //checks if sender is a participant for x conversation
        if (!participantConversationFacade.isParticipant(conversationId, userId)) {
            throw new SecurityException("sender is not a participant of {" + conversationId + "} conversaiond");
        }

        List<Message> messages = messageFacade.getMessagesForConversation(conversationId, limit, offset);


        return messages;
    }








        //checks email and strip them 
        private String checkAndValidateText(String message) {

        if (message == null || message.isBlank())
            throw new IllegalArgumentException("Text message is blank or null");

        return message.strip();
    }
    


}
