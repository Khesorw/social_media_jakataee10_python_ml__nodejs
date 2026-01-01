package com.app.corechat.api_pojos;

public class MeResDTO {
    public String username;
    public Long id;


    @Override
    public String toString() {
        return "{ username: " + this.username + " user id: " + this.id + " }";
    }
}
