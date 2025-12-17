package com.app.corechat.resources;

import com.app.corechat.business.UserRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("login")
public class Login {

    @Inject
    private UserRepository user;



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response validate(@FormParam("email") String email, @FormParam("password") String password) {
        if (user.validate(email, password)) {
            return Response.ok("found user").build();
        }

        return Response.status(404).build();
    }

    @GET
    @Path("getAll")
    public Response getAll() {
        
        return Response.ok(user.getAll()).build();
    }
    

}
