package com.app.corechat.resources;

import java.util.List;

import com.app.corechat.business.feed.search.FeedSearchService;
import com.app.corechat.entities.User;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("feed/search")
@Produces(MediaType.APPLICATION_JSON)
public class FeedSearchResource {


    @EJB
    private FeedSearchService feedSearchService;


    @GET
    @RolesAllowed("USER")
    public Response getUserList() {

        List<User> users;

        try {
            users = feedSearchService.listUsers();
            return Response.ok(users).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        

        
    }



}
