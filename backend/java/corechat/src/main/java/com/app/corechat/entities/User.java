package com.app.corechat.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="user_info")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_name", nullable=false, length=255)
    private String username;

    @Column(name = "user_pass",nullable=false,length=255)
    private String password;

    @Column(length=255)
    private String email;


    
    public User() {
        
    }

    public User(String email, Long id, String password, String username) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.username = username;
    }


    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    

    @Override
    public String toString() {
        
        return "{ user name: " + this.getUsername() + ", Email : " + this.getEmail() + " "+this.getId()+" }";
    }

}
