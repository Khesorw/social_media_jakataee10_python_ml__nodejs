package com.app.corechat.business.chat;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
@Stateless
public class ParticipantConversationFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;




    
    public boolean isParticipant(Long conversationId, Long senderId) {


           String jpql = "SELECT COUNT(cp1) "
            +"FROM ConversationParticipant cp1 "
            +"WHERE cp1.user.id= :senderId AND "
            +"cp1.conversation.id = :conversationId";


       Long count =  em.createQuery(jpql,Long.class)
                    .setParameter("senderId", senderId)
                    .setParameter("conversationId", conversationId)
                    .getSingleResult();


        return count > 0;
    }
    
}
