package com.app.corechat.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;


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


    

    

}
