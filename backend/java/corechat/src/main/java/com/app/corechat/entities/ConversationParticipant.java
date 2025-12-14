package com.app.corechat.entities;


import java.io.Serializable;
import jakarta.persistence.*;
@IdClass(ConversationParticipantId.class)
public class ConversationParticipant {
    

    @Id
    @ManyToMany(optional=false)
    @JoinColumn(name="conversation_id")
    private Conversation conversation;


    @Id
    @ManyToMany
    @JoinColumn(name="user_id")
    private User user;
    
}
