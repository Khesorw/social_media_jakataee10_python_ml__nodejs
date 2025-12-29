 package com.app.corechat.business.user;



import java.util.List;

import com.app.corechat.entities.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;


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
    

    public Long findUserIdByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("balnk or null email");
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<User> user = cq.from(User.class);

        cq.select(user.get("id"));

        cq.where(cb.equal(user.get("email"), email));

        Long userId = em.createQuery(cq).getSingleResult();

        return userId;
    }
    

    public User findById(Long id) {

        String jpql = "SELECT u FROM User u WHERE u.id = :id";

        var u = em.createQuery(jpql, User.class).setParameter("id", id).getResultList();
        if (u.isEmpty()) {
            return null;
        }
        return u.getFirst();
    }

    
}
