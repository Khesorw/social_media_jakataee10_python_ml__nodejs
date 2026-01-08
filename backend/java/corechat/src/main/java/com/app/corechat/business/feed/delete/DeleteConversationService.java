package com.app.corechat.business.feed.delete;

import java.util.logging.Logger;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;

@Stateless
public class DeleteConversationService {


    @EJB
    private DeleteConversationFacade deleteConversationFacade;
    private static final Logger LOG = Logger.getLogger(DeleteConversationService.class.getName());

    public Boolean deleteConversation(Long convId) {


        try {
            LOG.info("Serveic delete  the conversation: "+convId);


            deleteConversationFacade.deleteConversation(convId);
            return true;
        } catch (EntityNotFoundException e) {

            LOG.info("Error while trying to delete the conversation: "+convId);
            return false;

        }
        

        
    }
    
}
