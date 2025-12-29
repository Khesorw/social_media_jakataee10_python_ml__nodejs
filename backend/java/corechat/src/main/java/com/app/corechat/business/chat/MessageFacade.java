package com.app.corechat.business.chat;

import java.util.List;

import com.app.corechat.entities.Message;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MessageFacade {


    @PersistenceContext(unitName="MyPU")
    private EntityManager em;




    public void saveMessage(Message msg) {

        em.persist(msg);
    }
    
    public void deleteMsg(Message msg) {

        em.remove(em.merge(msg));
    }
    
    public void editMsg(Message msg) {

        em.merge(msg);
    }
    
    public Message findMessageById(Long id) {

        try{
        return (Message) em.createQuery("SELECT m FROM Message m WHERE m.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    } catch (Error e) {
        return null;
        }
    }
    

    public List<Message> findMessageByText(String text, Long convid) {
        
        return  em.createQuery("SELECT m FROM Message m WHERE m.messageText = :text AND m.conversation.id = :convid")
                .setParameter("text", text)
                .setParameter("convid", convid)
                .getResultList();
    }








}
