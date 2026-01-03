package com.app.corechat.business.feed;

import java.util.List;

import com.app.corechat.api_pojos.FeedMsgSummaryDTO;
import com.app.corechat.entities.Conversation;
import com.app.corechat.entities.ConversationParticipant;
import com.app.corechat.entities.Message;
import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Stateless
public class FeedConversationFacade {
    
    @PersistenceContext(unitName="MyPU")
    private EntityManager em;

    

    /**
     * Gets list of all the conversation that user is part of then for each object it contains the otherUser name, the last message,
     * senderId, creation-time using multiple joins later to be mapped into FeedConversationSummaryDTO in criteriabuilder
     * @param userId
     * @return List of conversation that user is part of
     */
    public List<FeedMsgSummaryDTO> getConversationListDTO(Long userId) {

        //now using joins instead mutipleselect
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<FeedMsgSummaryDTO> cq =
                cb.createQuery(FeedMsgSummaryDTO.class);

        
        Root<Conversation> conversation = cq.from(Conversation.class);

        // JOINS
        Join<Conversation, ConversationParticipant> mePart = conversation.join("participant");
                

        Join<Conversation, ConversationParticipant> otherPart = conversation.join("participant");
                

        Join<ConversationParticipant, User> otherUser = otherPart.join("user");

        Join<Conversation, Message> message =
                conversation.join("messages");


        Subquery<Long> lastMessageId = cq.subquery(Long.class);
        Root<Message> m2 = lastMessageId.from(Message.class);

        lastMessageId.select(cb.max(m2.get("id")))
                    .where(cb.equal(m2.get("conversation"), conversation));


        cq.select(cb.construct(
            FeedMsgSummaryDTO.class,
            conversation.get("id"),
            otherUser.get("username"),
            message.get("messageText"),
            message.get("sender").get("id"),
            message.get("createdAt")
        ));

        cq.where(
            cb.equal(mePart.get("user").get("id"), userId),
            cb.notEqual(otherUser.get("id"), userId),
            cb.equal(message.get("id"), lastMessageId)
        );

        cq.distinct(true);
        List<FeedMsgSummaryDTO> ms = em.createQuery(cq).getResultList();

 
       return ms;


    }
    
}
