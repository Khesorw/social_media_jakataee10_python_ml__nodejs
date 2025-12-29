package com.app.corechat.business.chat;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MessageService {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;



    public void sendMessage(Long conversationId, Long senderId, String txt) {
        
    }



}
