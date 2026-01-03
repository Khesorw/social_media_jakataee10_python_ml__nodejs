package com.app.corechat.api_pojos;

public class UserDTO {

    
    private Long id;
    private String email;
    private String username;

    public UserDTO(String email, Long id, String username) {
        this.email = email;
        this.id = id;
        this.username = username;
    }
    



    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }



}
