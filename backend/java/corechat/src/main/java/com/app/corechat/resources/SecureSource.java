package com.app.corechat.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("secure")
@RolesAllowed("USER")
public class SecureSource {
    
    @Inject
    SecurityContext securtiyContext;
    @GET
    public Response test() {
        
        return Response.ok("Hello"+securtiyContext.getCallerPrincipal().getName()).build();
    }
}
