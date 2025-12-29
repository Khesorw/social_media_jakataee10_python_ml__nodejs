package com.app.corechat.business.chat;

import com.app.corechat.business.user.UserFecade;
import com.app.corechat.entities.Conversation;
import com.app.corechat.entities.Message;
import com.app.corechat.entities.User;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.SecurityContext;

@Stateless
public class MessageService {

    @PersistenceContext(unitName="MyPU")
    @SuppressWarnings("unused")
    private EntityManager em;

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
    public void sendMessage(Long conversationId, String txt) {
        //cheap validation 
        if (conversationId == null) {
            throw new IllegalArgumentException("conversationid is null");

        }
        
        String msg = checkAndValidateText(txt);

        
        //getting sender id from their email ()
        String email = securityContext.getCallerPrincipal().getName();
        Long userId = userFecade.findUserIdByEmail(email);
        

        //validate conversation exist
        if (!(conversationFacade.findById(conversationId))) {
            throw new EntityNotFoundException("conversation " + conversationId + " does not exist");
        }

        //checks if sender is a participant for x conversation
        if (!participantConversationFacade.isParticipant(conversationId, userId)) {
            throw new SecurityException("sender is not a participant of {" + conversationId + "} conversaiond");
        }

        Conversation c = em.getReference(Conversation.class, conversationId);
        User u = em.getReference(User.class, userId);


        Message message = new Message();
        message.setConversation(c);
        message.setSender(u);
        message.setMessageText(msg);


        messageFacade.saveMessage(message);


    }
    



    private String checkAndValidateText(String message) {

        if (message == null || message.isBlank()) 
            throw new IllegalArgumentException("Text message is blank or null");
        
        return message.strip();
    }



}
