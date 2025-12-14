package com.app.corechat.entities;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="messages")
public class Message {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="message_txt",nullable=false)
    private String messageText;

    @Column(name="created_at")
    private OffsetDateTime createdAt;


    @ManyToOne(optional = false)
    @JoinColumn(name="conversation_id")
    private Conversation conversation;


    @ManyToOne(optional=false)
    @JoinColumn(name="sender_id")
    private User sender;


    
}
