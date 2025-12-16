package com.app.corechat.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="conversation_participant")
@IdClass(ConversationParticipantId.class)
public class ConversationParticipant {
    

    @Id
    @ManyToOne(optional=false)
    @JoinColumn(name="conversation_id")
    private Conversation conversation;


    @Id
    @ManyToOne(optional=false)
    @JoinColumn(name="user_id")
    private User user;
    
}
