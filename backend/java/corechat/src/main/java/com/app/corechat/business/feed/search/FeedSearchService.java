package com.app.corechat.business.feed.search;

import java.util.List;

import com.app.corechat.business.user.UserFecade;
import com.app.corechat.entities.User;

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


    public List<User> listUsers() {


        String email = securityContext.getCallerPrincipal().getName();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("feedSearch invalied email or unauthenticaed");
        }

        Long id = userFecade.findUserIdByEmail(email);
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("invalied id or unauthenticaed from feedSearch");

        }
        
        List<User> users = feedSearchFacade.findUsers(email, id);

        

        
        return users;
    }
    
}
