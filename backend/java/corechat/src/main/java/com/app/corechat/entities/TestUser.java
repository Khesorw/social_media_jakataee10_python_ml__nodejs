package com.app.corechat.entities;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="test_user")
public class TestUser {
 
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;


    private String username;
    private String email;

    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    
    public TestUser() {
    }

    public TestUser(LocalDateTime createdAt, String email, long id, String username) {
        this.createdAt = createdAt;
        this.email = email;
        this.id = id;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @jakarta.persistence.PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    

    

}
