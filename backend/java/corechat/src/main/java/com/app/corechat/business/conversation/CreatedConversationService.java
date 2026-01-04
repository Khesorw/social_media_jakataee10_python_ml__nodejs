package com.app.corechat.business.conversation;



import com.app.corechat.business.user.UserFecade;
import com.app.corechat.entities.User;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;

@Stateless
public class CreatedConversationService {


    @EJB
    CreateConversationFacade createConversationFacade;

    @EJB
    UserFecade userFecade;

    @Inject
    SecurityContext securityContext;


    public Long createConvRoom(Long otherUserId) {

        Long convExist = null;
        if (otherUserId == null || otherUserId <= 0) {
            throw new IllegalArgumentException("Invalid otherUser id or null email");
        }


        String myEmail = securityContext.getCallerPrincipal().getName();
         if (myEmail == null || myEmail.isBlank() ) {
            throw new IllegalArgumentException("Invalid email or null email");
        }

        Long myUsserId = userFecade.findUserIdByEmail(myEmail);

        convExist = createConversationFacade.findConversationExistBtwUsers(myUsserId, otherUserId);
        
        if (convExist == null) {


            User userMe = userFecade.findById(myUsserId);
            User otherUser = userFecade.findById(otherUserId);

            if (userMe == null || otherUser == null) {
                throw new SecurityException("un-authenticated users ");
            }
            else {

                Long newConvId = createConversationFacade.createConversationParticipants(userMe, otherUser);

                return newConvId;
            }
            
            
        }//if()
          
        return null;
    }



}
