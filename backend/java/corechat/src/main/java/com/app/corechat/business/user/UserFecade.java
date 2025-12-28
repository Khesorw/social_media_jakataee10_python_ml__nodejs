 package com.app.corechat.business.user;



import java.util.List;

import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;


@Stateless
public class UserFecade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;



    public void create(User u) {

        em.persist(u);

    }
  
    public List<User> findUserByEmail(String email) {

        String queryString = "SELECT u FROM User u where u.email = :email";

        TypedQuery<User> query = em.createQuery(queryString, User.class);

        query.setParameter("email", email);

        return query.getResultList();
   
   
    }

    
}
