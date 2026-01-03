package com.app.corechat.business.feed.search;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.UserDTO;
import com.app.corechat.business.user.UserFecade;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

@Stateless
public class FeedSearchService {

    @EJB
    private FeedSearchFacade feedSearchFacade;

    @Inject
    private SecurityContext securityContext;

    @EJB
    private UserFecade userFecade;

    static Logger LOG = Logger.getLogger(FeedSearchService.class.getName());

    public List<UserDTO> listUsers(String input) {

        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("blank query input email or null");

        }

        String email = securityContext.getCallerPrincipal().getName();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("feedSearch invalied email or unauthenticaed");
        }

        Long id = userFecade.findUserIdByEmail(email);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("invalied id or unauthenticaed from feedSearch");

        }
        
        List<UserDTO> users = feedSearchFacade.findUsers(input, id);

        if (users.isEmpty()) {
            LOG.warning("Empty user's list from search: ");
        }
        else {
            users.forEach(each -> LOG.info("User found "+each.toString()));
        }
        
        return users;
    }
    
}
