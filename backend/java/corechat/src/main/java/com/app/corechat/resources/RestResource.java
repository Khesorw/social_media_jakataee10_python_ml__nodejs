/********************************************************************************
 * Copyright (c) 10/19/2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Eclipse Distribution License
 * v1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ********************************************************************************/
package com.app.corechat.resources;

import com.app.corechat.business.user.UserService;
import com.app.corechat.entities.TestUser;
import com.app.corechat.entities.User;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.core.Response;

@Path("hello")
public class RestResource {

    @Inject
    private PasswordHash passwordHash;

    @EJB
    private UserService userService;

    @jakarta.persistence.PersistenceContext(unitName = "MyPU")
    private EntityManager em;
    @GET
    public Response hel() {
        return Response.ok("Hello from Response").build();
    }

    @Path("test")
    @GET
    @Produces(APPLICATION_JSON)
    public Response test() {
        return Response
                .ok(em.createQuery("select t from TestUser t order by t.email asc", TestUser.class).getResultList())
                .build();

    }
    
    @Path("wok")
    @GET
    public Response wok() {

        return Response.ok("hello from wok").build();
    }



    @Path("genhash")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hash(@QueryParam("p") String password) {

        if (password == null || password.isBlank())
            return "provide ?p=yourpassword";

        return passwordHash.generate(password.toCharArray());
    }


    

    @Path("eml")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserByEmail(@QueryParam("email") String email) {

        if (email == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        String queryString = "SELECT u FROM User u where u.email = :email";

        TypedQuery<User> query = em.createQuery(queryString, User.class);

        query.setParameter("email", email);

        return Response.ok(query.getResultList()).build();
    }


    @Path("regist")
    @POST
    public Response signup(User user) {
        userService.persist(user);

        return Response.status(Response.Status.CREATED).build();
    }
    

    @Path("check")
    @GET
    public Response isPart(@QueryParam("partId") Long partId, @QueryParam("usId") Long usId) {
        String jpql = "SELECT COUNT(cp1) "
                + "FROM ConversationParticipant cp1 "
                + "WHERE cp1.user.id= :senderId AND "
                + "cp1.conversation.id = :conversationId";

        Long count = em.createQuery(jpql, Long.class)
                .setParameter("senderId", usId)
                .setParameter("conversationId", partId)
                .getSingleResult();

        String c = String.valueOf(count > 0);

        return Response.ok(c).build();

    }

    
    @Path("findid")
    @GET
    public Response getidbyemail(@QueryParam("eml") String eml) {

          if (eml == null || eml.isBlank()) {
            throw new IllegalArgumentException("balnk or null email ?eml=?");
        }

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);

        Root<User> user = cq.from(User.class);

        cq.select(user.get("id"));

        cq.where(cb.equal(user.get("email"), eml));

        Long userId = em.createQuery(cq).getSingleResult();
        

        if (userId != null) {
            return Response.ok(String.valueOf(userId)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }




}
