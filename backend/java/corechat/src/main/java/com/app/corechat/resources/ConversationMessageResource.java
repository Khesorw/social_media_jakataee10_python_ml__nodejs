package com.app.corechat.resources;
import java.util.List;

import com.app.corechat.api_pojos.MessageDTO;
import com.app.corechat.business.chat.MessageService;
import com.app.corechat.entities.Message;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("conversation/{convId}/messages")
@jakarta.ws.rs.Produces(MediaType.APPLICATION_JSON)
public class ConversationMessageResource {

    @EJB
    private MessageService messageService;

    /**
     * Return all the message for the "convId" room
     * @param convId
     * @param limit
     * @param offset
     * @return List<Message> for x conversation romm
     */
    @GET
    @RolesAllowed("USER")
    public Response retriveMessages(
            @PathParam("convId") Long convId,
            @DefaultValue("50") @QueryParam("limit") int limit,
            @DefaultValue("0") @QueryParam("offset") int offset) {
        
        if (convId == null || convId == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("invalid conversationid boqa").build();
        }

                
        try {
            List<Message> messages = messageService.getMessages(convId, limit, offset);


        List<MessageDTO> dtoList =
        messages.stream()
            .map(m -> {
                MessageDTO dto = new MessageDTO();
                dto.id = m.getId();
                dto.text = m.getMessageText();
                dto.createdAt = m.getCreatedAt();
                dto.senderId = m.getSender().getId();
                dto.senderEmail = m.getSender().getEmail();
                return dto;
            })
            .toList();

        return Response.ok(dtoList).build();


 

            

            // return Response.ok(messages).build();

        } 
        catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } 
        catch (EntityNotFoundException en) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } 
        catch (SecurityException se) {

            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        
        }

}
