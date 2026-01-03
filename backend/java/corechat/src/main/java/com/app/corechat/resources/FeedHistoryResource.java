package com.app.corechat.resources;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.FeedMsgSummaryDTO;
import com.app.corechat.business.feed.FeedConversationService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.resource.spi.SecurityException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("feedHistory")
@Produces(MediaType.APPLICATION_JSON)
public class FeedHistoryResource {

    private static final Logger LOG = Logger.getLogger(FeedHistoryResource.class.getName());

    @EJB
    private FeedConversationService feedConversationService;


    @RolesAllowed("USER")
    @GET
    public Response getFeed() {

        List<FeedMsgSummaryDTO> msgList;

        try {
            msgList = feedConversationService.getContcList();
            if (msgList == null || msgList.isEmpty()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return Response.ok(msgList).build();
        } catch (SecurityException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

            return Response.status(Response.Status.BAD_REQUEST).build();
        
    }

    
    
}
