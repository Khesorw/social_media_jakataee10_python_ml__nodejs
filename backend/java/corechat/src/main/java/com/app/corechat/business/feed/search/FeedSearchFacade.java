package com.app.corechat.business.feed.search;

import java.util.List;

import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class FeedSearchFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;



    public List<User> findUsers(String input, Long userId) {


        String jpql;
        TypedQuery<User> tq;
        
        boolean isEmail = input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$");
        if (!isEmail) {
            jpql = "SELECT u.username, u.id, u.email FROM User u WHERE u.username = :input AND u.id <> :userId";
            tq = em.createQuery(jpql, User.class);
            tq.setParameter("input", input + "%");
            
        }
            jpql = "SELECT u.username, u.id, u.email FROM User u WHERE u.email = :input AND u.id <> :userId";
            tq = em.createQuery(jpql, User.class);
            tq.setParameter("input", input);
            tq.setParameter("userId", userId);

      
        

        return tq.getResultList();
    }

    
}
