package com.app.corechat.business;

import java.util.List;

import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserRepository {
    @PersistenceContext(unitName="MyPU")
    private EntityManager em;


    public boolean validate(String email, String password) {
        try {
            User user = null;
            user = em.createQuery("select u from User u where u.email = :email AND u.password = :pass", User.class)
                    .setParameter("email", email)
                    .setParameter("pass", password)
                    .getSingleResult();

            return user != null;
        } 
        catch (jakarta.persistence.NoResultException e) 
        {
            
            return false;
        }
    } 

     public List<User> getAll(){

        return em.createQuery("select u from User u order by u.id",User.class).getResultList();
     }

        

        
}


