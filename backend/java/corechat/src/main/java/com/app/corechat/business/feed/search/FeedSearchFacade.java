package com.app.corechat.business.feed.search;

import java.util.List;
import java.util.logging.Logger;

import com.app.corechat.api_pojos.UserDTO;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class FeedSearchFacade {

    @PersistenceContext(unitName="MyPU")
    private EntityManager em;

    static Logger LOG = Logger.getLogger(FeedSearchFacade.class.getName());

    public List<UserDTO> findUsers(String input, Long userId) {


        String jpql;
        TypedQuery<UserDTO> tq;

        LOG.info("User Id is: "+userId);
        
        boolean isEmail = input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$");
        if (!isEmail) {
            jpql = "SELECT new com.app.corechat.api_pojos.UserDTO(u.email, u.id, u.username) FROM User u WHERE LOWER(u.username) LIKE :input AND u.id <> :userId";
            tq = em.createQuery(jpql, UserDTO.class);
            tq.setParameter("input", input.toLowerCase() + "%");
            tq.setParameter("userId", userId);
            LOG.info("It is not email " + input);
            List<UserDTO> namedUsers = tq.getResultList();
            return namedUsers;

        }
        
            jpql = "SELECT new com.app.corechat.api_pojos.UserDTO(u.email, u.id, u.username) FROM User u WHERE LOWER(u.email) = :input AND u.id <> :userId";
            tq = em.createQuery(jpql, UserDTO.class);
            tq.setParameter("input", input.toLowerCase());
            tq.setParameter("userId", userId);
            LOG.info("It is email  " + input);
            return tq.getResultList();
    }

    
}
