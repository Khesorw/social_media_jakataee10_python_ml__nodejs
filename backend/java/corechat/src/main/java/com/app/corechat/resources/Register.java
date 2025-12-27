package com.app.corechat.resources;

import jakarta.annotation.security.PermitAll;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Path;

@PermitAll
@Path("signup")
public class Register {
    @PersistenceContext(name="MyPU")
    private EntityManager em;
    
}
