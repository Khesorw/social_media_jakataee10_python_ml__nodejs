package com.app.corechat.resources;



import java.sql.Connection;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("db-test")
public class DbTestResource {

    // This checks if the JNDI name is correct
    @Resource(lookup = "jdbc/myappdb")
    private DataSource dataSource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkConnection() {
        if (dataSource == null) {
            return Response.status(500).entity("DataSource injection failed! Check JNDI name.").build();
        }

        try (Connection conn = dataSource.getConnection()) {
            String dbName = conn.getMetaData().getDatabaseProductName();
            return Response.ok("Connection Success! Connected to: " + dbName).build();
        } catch (Exception e) {
            return Response.status(500).entity("JNDI found, but Connection failed: " + e.getMessage()).build();
        }
    }
}
 
    

