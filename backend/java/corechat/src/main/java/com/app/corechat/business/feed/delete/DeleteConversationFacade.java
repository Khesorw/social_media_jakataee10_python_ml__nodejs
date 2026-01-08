package com.app.corechat.business.feed.delete;

import com.app.corechat.entities.Conversation;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class DeleteConversationFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;



    public void deleteConversation(Long id) {
        
        Conversation conversation = em.find(Conversation.class, id);

        if (conversation != null) {
            em.remove(conversation);
            em.flush();
        } else {
             throw new EntityNotFoundException("conversation with " + id + " does not exist");
            
        }
        
       
    }
    
}
