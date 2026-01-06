package com.app.corechat.api_pojos;

public class UserDTO {

    
    private Long id;
    private String email;
    private String username;
    private Boolean existing;
    private Long convId = null;

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

    public Boolean  getExisting() {
        return existing;
    }

    public void setExisting(Boolean existing) {
        this.existing = existing;
    }

    public Long getConvId() {
        return convId;
    }

    public void setConvId(Long convId) {
        this.convId = convId;
    }



}
