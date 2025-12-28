package com.app.corechat.business.user;

import com.app.corechat.entities.User;
import com.app.corechat.exceptions.DuplicateEmailException;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;

@Stateless
public class UserService {


    @EJB
    UserFecade userFecade;

    @Inject
    private PasswordHash passwordHash;


    public void persist(User user) {

        if (user.getEmail() == null || user.getUsername() == null || user.getPassword() == null) {
        throw new IllegalArgumentException("Null values");
        }
    
        if (user.getEmail().isBlank() || user.getUsername().isBlank() || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("One of these attributes is blank {email, username, password}");
        }

    
      
        //normalization
        user.setEmail(user.getEmail().toLowerCase());

        //stirping email and username
        user.setUsername(user.getUsername().strip());
        user.setEmail(user.getEmail().replace(" ", ""));

         //checks for duplicate email
         if (userFecade.findUserByEmail(user.getEmail()).isEmpty()) {
             throw new DuplicateEmailException("Duplicate email ");
           
        }


        //hashing the password
        String plainPassword = user.getPassword();
        String hashedPassword = passwordHash.generate(plainPassword.toCharArray());
        user.setPassword(hashedPassword);

        

    
     
        userFecade.create(user);
      
       
        
    }
    
}
