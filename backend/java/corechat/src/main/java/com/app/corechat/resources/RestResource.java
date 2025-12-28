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

import com.app.corechat.entities.TestUser;
import com.app.corechat.entities.User;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.security.enterprise.identitystore.PasswordHash;
import jakarta.ws.rs.GET;
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




}
