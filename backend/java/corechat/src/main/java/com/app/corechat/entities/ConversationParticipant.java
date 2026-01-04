package com.app.corechat.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="participant_conversation")
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

    public ConversationParticipant() {

    }
    
    
    public ConversationParticipant(Conversation conversation, User user) {
        this.conversation = conversation;
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public User getUser() {
        return user;
    }


    
    
}
