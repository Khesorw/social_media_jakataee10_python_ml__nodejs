package com.app.corechat.business.chat;

import com.app.corechat.entities.Conversation;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ConversationFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;






    /**
     * 
     * @param id
     * @return conversation {id,created_at} for the given id if exist 
     */
    public Conversation find(Long id) {
        return em.getReference(Conversation.class, id);
    }
    

    /**
     * 
     * @param id
     * @return true if the conversation exist for the id that being passed as arg else wrong
     */
    public boolean doesExist(Long id) {

        String jpql = "SELECT COUNT(c) FROM Conversation c WHERE c.id = :id";

        Long c = em.createQuery(jpql, Long.class).setParameter("id", id).getSingleResult();

        return c > 0;
    }



    


    
    





}
