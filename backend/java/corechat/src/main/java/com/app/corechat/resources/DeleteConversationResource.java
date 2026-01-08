package com.app.corechat.resources;

import java.util.logging.Logger;

import com.app.corechat.business.feed.delete.DeleteConversationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("conversation/delete/{convId}")
public class DeleteConversationResource {


    @EJB
    private DeleteConversationService conversationService;


    private static final Logger LOG = Logger.getLogger(DeleteConversationResource.class.getName());


    @RolesAllowed("USER")
    @DELETE
    public Response delete(@PathParam("convId") Long id) {

        LOG.info("id for delete is: " + id);
        
        try {
            Boolean success = conversationService.deleteConversation(id);
            if (success) {
                return Response.ok("conversation " + id + " deleted").build();

            }
            return Response.status(Response.Status.BAD_REQUEST).build();

        } 
        catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
            
        }
        
    }
}
