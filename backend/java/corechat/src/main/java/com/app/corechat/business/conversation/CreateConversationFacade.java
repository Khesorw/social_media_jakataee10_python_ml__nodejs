package com.app.corechat.business.conversation;

import com.app.corechat.entities.Conversation;
import com.app.corechat.entities.ConversationParticipant;
import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CreateConversationFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;


    public Long createConversationParticipants(User me, User otherUser) {

        Conversation conversation = new Conversation();

        em.persist(conversation);
        em.flush();
        
        ConversationParticipant cp1 = new ConversationParticipant(conversation, me);
        em.persist(cp1);


        ConversationParticipant cp2 = new ConversationParticipant(conversation, otherUser);
        em.persist(cp2);

        Long convId = conversation.getId();
        return convId;
        
    }


    public Long findConversationExistBtwUsers(Long me, Long other) {

        try {
            
            String jpql = "SELECT cp.conversation.id FROM ConversationParticipant cp WHERE cp.user.id IN (:me, :other) "
                    + " GROUP BY cp.conversation.id HAVING COUNT(cp.conversation.id) = 2";
            
            return em.createQuery(jpql, Long.class)
                   .setParameter("me", me)
                   .setParameter("other", other)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }

        
        
    }

    



}







