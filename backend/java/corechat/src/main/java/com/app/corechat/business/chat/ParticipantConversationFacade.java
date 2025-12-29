package com.app.corechat.business.chat;


import java.util.List;

import com.app.corechat.entities.ConversationParticipant;
import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
@Stateless
public class ParticipantConversationFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;




    
    public boolean isParticipant(Long conversationId, Long senderId) {

        String jpql = "SELECT COUNT(cp1) "
                + "FROM ConversationParticipant cp1 "
                + "WHERE cp1.user.id= :senderId AND "
                + "cp1.conversation.id = :conversationId";

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("senderId", senderId)
                .setParameter("conversationId", conversationId)
                .getSingleResult();

        return count > 0;
    }
    

    public List<ConversationParticipant> findByUser(User user) {


        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<ConversationParticipant> cq = cb.createQuery(ConversationParticipant.class);

        Root<ConversationParticipant> root = cq.from(ConversationParticipant.class);

        cq.where(cb.equal(root.get("user"), user));

        
        

        return em.createQuery(cq).getResultList();
    }
    
}
