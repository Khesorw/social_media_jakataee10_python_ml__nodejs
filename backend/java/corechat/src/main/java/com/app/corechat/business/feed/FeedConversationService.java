package com.app.corechat.business.feed;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.FeedMsgSummaryDTO;
import com.app.corechat.business.user.UserFecade;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.resource.spi.SecurityException;
import jakarta.security.enterprise.SecurityContext;

@Stateless
public class FeedConversationService {


    @Inject
    private SecurityContext securityContext;

    @EJB
    private FeedConversationFacade feedConversationFacade;

    @EJB
    private UserFecade userFecade;

    private static final Logger LOG = Logger.getLogger(FeedConversationService.class.getName());

    public List<FeedMsgSummaryDTO> getContcList() throws SecurityException {


        LOG.info("Getting stareted to get email for contact summary history: ");

        String email = securityContext.getCallerPrincipal().getName();

        LOG.info("Email from FeedConversationServiceIS : " + email);
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("blank email");
        }

        Long userId = userFecade.findUserIdByEmail(email);
        if (userId == null || userId <= 0) {
            throw new SecurityException("unauthenticated user from feedconversationService LL");
        }


        return  feedConversationFacade.getConversationListDTO(userId);
    }


 


 


    

}
