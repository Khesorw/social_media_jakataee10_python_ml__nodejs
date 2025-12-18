package com.app.corechat.resources;

import java.util.logging.Logger;

import com.app.corechat.api_pojos.LoginRequest;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;




@Path("login")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)    
public class Login {



    @Inject
    private SecurityContext securityContext;

    private static final Logger LOG = Logger.getLogger(Login.class.getName());



    @POST
    public Response validate(
    @Context HttpServletRequest request,
            @Context HttpServletResponse response, LoginRequest loginRequest) {
        
            

        LOG.info(()->"Logging Attempt for the user "+loginRequest.getEmail());
          
        // Credential credentials = new UsernamePasswordCredential(loginRequest.getEmail(),
        //         new Password(loginRequest.getPassword()));
        UsernamePasswordCredential credentials = 
                                 new UsernamePasswordCredential(
                                                loginRequest.getEmail(),
                                                     new Password(loginRequest.getPassword()));

                                

        //attempt to authenticate
        AuthenticationStatus status = securityContext.authenticate(
            request,
            response,
                AuthenticationParameters.withParams().credential(credentials));


        switch (status) {
            
            case SUCCESS:
                return Response.ok().build();
            
            case SEND_FAILURE:
                return Response.status(Response.Status.UNAUTHORIZED).build();

            case NOT_DONE:
                return Response.status(Response.Status.BAD_REQUEST).build();
            
            default:
                return Response.serverError().build();
            
            
        }

        
        // if (status == AuthenticationStatus.SUCCESS) {
        //     return Response.ok()
        //             .entity("{\"message\": \"Authenticated\"}")
        //             .build();
        // }

        // //if failed
        // return Response.status(Response.Status.UNAUTHORIZED).entity("{\"message\": \"Invalid email or passwrod\"}").build();
                
                                

    }//validate()


    @OPTIONS
    public Response prefligh() {
        return Response.ok().build();
    }

   

}
