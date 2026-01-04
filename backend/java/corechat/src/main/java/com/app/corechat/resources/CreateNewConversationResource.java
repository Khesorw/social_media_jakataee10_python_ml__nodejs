package com.app.corechat.resources;

import java.util.logging.Logger;

import com.app.corechat.api_pojos.CreateConvDTO;
import com.app.corechat.api_pojos.CreateConvResponseDTO;
import com.app.corechat.business.conversation.CreatedConversationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("create/convroom")
public class CreateNewConversationResource {

    private static final Logger LOG = Logger.getLogger(CreateNewConversationResource.class.getName());

    @EJB
    private CreatedConversationService createdConversationService;

    @POST
    @RolesAllowed("USER")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewConvRoom(CreateConvDTO createConvDTO) {


        if (createConvDTO == null) {
            LOG.info("Json binding failed no CreateConvDTO ");
            return Response.status(Response.Status.BAD_REQUEST).build();
        } 
        else {
            LOG.info("Json binding success CreateConvDTO other usedId"+createConvDTO.getOtherUserid());
            Long newConvId = createdConversationService.createConvRoom(createConvDTO.getOtherUserid());

            return Response.status(Response.Status.CREATED).entity(new CreateConvResponseDTO(newConvId)).build();
            
        }


    }

    
    

}
