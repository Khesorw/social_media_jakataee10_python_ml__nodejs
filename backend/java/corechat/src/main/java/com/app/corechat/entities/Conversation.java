package com.app.corechat.entities;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name="conversation")
public class Conversation {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="created_at", insertable=false,updatable=false)
    private OffsetDateTime createdAt;


    @OneToMany(mappedBy="conversation",cascade=CascadeType.ALL)
    private List<ConversationParticipant> participant;


    @OneToMany(mappedBy="conversation",cascade=CascadeType.ALL)
    private List<Message> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ConversationParticipant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<ConversationParticipant> participant) {
        this.participant = participant;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    
    
}
