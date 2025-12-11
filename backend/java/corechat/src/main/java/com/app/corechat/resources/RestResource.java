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

import jakarta.ws.rs.Produces;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("hello")
public class RestResource {   

    
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
        return Response.ok(em.createQuery("select t from TestUser t order by t.email asc", TestUser.class).getResultList()).build();
    
    }
}
