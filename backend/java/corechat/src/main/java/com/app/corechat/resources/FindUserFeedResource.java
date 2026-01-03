package com.app.corechat.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("lookup")
@RolesAllowed("USER")
public class FindUserFeedResource {

    @GET
    public Response findUsers(@QueryParam("searchQuery") String nameOrEmail) {
        



        return Response.status(Response.Status.NO_CONTENT).build();
    }


    
}
