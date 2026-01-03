package com.app.corechat.resources;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.UserDTO;
import com.app.corechat.business.feed.search.FeedSearchService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("feed/search")
@Produces(MediaType.APPLICATION_JSON)
public class FeedSearchResource {


    @EJB
    private FeedSearchService feedSearchService;

    private static final Logger LOG = Logger.getLogger(FeedSearchResource.class.getName());


    @GET
    @RolesAllowed("USER")
    public Response getUserList(@QueryParam("input") String input) {

        LOG.info("GOT input from frontend "+input);

        List<UserDTO> users;

        try {
            users = feedSearchService.listUsers(input);
            return Response.ok(users).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        

        
    }



}
