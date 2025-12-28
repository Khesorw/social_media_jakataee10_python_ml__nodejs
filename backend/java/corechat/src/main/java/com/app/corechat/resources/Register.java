package com.app.corechat.resources;

import com.app.corechat.business.user.UserService;
import com.app.corechat.entities.User;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.logging.Logger;

@PermitAll
@Path("/signup")
public class Register {


    private final static Logger LOG = Logger.getLogger(Register.class.getName());

    @EJB
    UserService userService;

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signup(User user) {
        userService.persist(user);
        
        return Response.status(Response.Status.CREATED).build();
    }
    
}
