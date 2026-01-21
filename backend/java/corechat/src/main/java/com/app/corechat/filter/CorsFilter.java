 package com.app.corechat.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching  // ← Important: Apply to all requests including OPTIONS
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {

        // Get the origin from the request
        String origin = requestContext.getHeaderString("Origin");

        // ✅ FIXED: Allow requests from any origin (development mode)
        // This allows localhost:5173, 192.168.1.105:5173, or served from GlassFish
        if (origin != null) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", origin);
        } else {
            // Fallback: allow all
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        }

        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        responseContext.getHeaders().add("Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization, X-Requested-With");

        responseContext.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");

        responseContext.getHeaders().add("Access-Control-Max-Age", "3600");

        // ✅ Handle preflight OPTIONS requests
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(Response.Status.OK.getStatusCode());
        }
    }
}
