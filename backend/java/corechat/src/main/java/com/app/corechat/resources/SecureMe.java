package com.app.corechat.resources;

import java.util.logging.Logger;

import com.app.corechat.api_pojos.MeResDTO;
import com.app.corechat.business.user.UserFecade;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
@Path("me")
@Produces(MediaType.APPLICATION_JSON)
public class SecureMe {

    private static final Logger LOG = Logger.getLogger(SecureMe.class.getName());

    @Inject
    private SecurityContext securityContext;
        @EJB
    private UserFecade userFecade;

    @GET
    @RolesAllowed("USER")
    public Response me() {
        

        if (securityContext.getCallerPrincipal() == null) {
            LOG.info("FROM ME securityContext caller praincipal is null ");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } else {

        String username = securityContext.getCallerPrincipal().getName();
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        //gets userid form User through email
        Long id = userFecade.findUserIdByEmail(username);
        if (id == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        LOG.info("me called by authenticated user: " + username);
        LOG.info("me called by authenticated user: " + id);


            
        MeResDTO meResDTO = new MeResDTO();
        meResDTO.id = id;
        meResDTO.username = username;

        LOG.info("me called by authenticated user: "+meResDTO.toString());




            return Response.ok()
                    .entity(meResDTO)
                    .build();
        }
    }
}












// package com.app.corechat.resources;

// import java.util.logging.Logger;

// import jakarta.security.enterprise.SecurityContext;
// import jakarta.ws.rs.GET;
// import jakarta.ws.rs.Path;
// import jakarta.ws.rs.core.Context;
// import jakarta.ws.rs.core.Response;

// @Path("me")

// public class SecureMe {

//     private static final Logger LOG = Logger.getLogger(SecureMe.class.getName());

//     @GET
//     public Response me(@Context SecurityContext context) {

//         LOG.info("FROM SECURE ME " + context.getCallerPrincipal().getName());
//         LOG.info(context.getCallerPrincipal() == null ? "context.get pricipal is null" : "Caller get principlat is not null");
        

//         return Response.ok(context.getCallerPrincipal().getName()).build();
        
//     }
    


// }
