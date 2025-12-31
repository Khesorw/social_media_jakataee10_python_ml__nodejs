package com.app.corechat.business.chat;

import java.util.List;

import com.app.corechat.entities.Message;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Stateless
public class MessageFacade {


    @PersistenceContext(unitName="MyPU")
    private EntityManager em;



    /**
     * persist the passed message
     * @param msg
     */
    public void saveMessage(Message msg) {

        em.persist(msg);
    }
    
    /**
     * Delete specified message passed in the argument
     * 
     * @param msg
     */
    public void deleteMsg(Message msg) {

        em.remove(em.merge(msg));
    }
    
    /**
     * edit the specified message
     * @param msg
     */
    public void editMsg(Message msg) {

        em.merge(msg);
    }
    

    /**
     * Find a message by its id if exists. (for test purposes)
     * @param id
     * @return Message
     */
    public Message findMessageById(Long id) {

        try {
            return (Message) em.createQuery("SELECT m FROM Message m WHERE m.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Error e) {
            return null;
        }
    }
    
    
    /**
     * Find specific text from the specified conversation
     * @param text
     * @param convid
     * @return List of messages for the given text for example conversation x has many Hello,
             * if "Hello" is passed as argument for text then the method will return a list containing all the "Hellos"
     */
    public List<Message> findMessageByText(String text, Long convid) {

        return em.createQuery("SELECT m FROM Message m WHERE m.messageText = :text AND m.conversation.id = :convid")
                .setParameter("text", text)
                .setParameter("convid", convid)
                .getResultList();
    }


    /**
     * Find all the messages of the specified conversation room (convId)
     * @param conversation
     * @return List of Messages
     */
    public List<Message> getMessagesForConversation(Long conversationId, int limit, int offset) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> message = cq.from(Message.class);
        cq.select(message);

        cq.where(cb.equal(message.get("conversation").get("id"), conversationId));
        cq.orderBy(cb.asc(message.get("createdAt")));

        TypedQuery<Message> query = em.createQuery(cq);
        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return query.getResultList();
    }
    

    








}
