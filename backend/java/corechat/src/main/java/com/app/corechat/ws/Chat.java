package com.app.corechat.ws;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.CloseReason;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;

// REMOVED @DeclareRoles and @RolesAllowed
@ServerEndpoint(
    value = "/chat",
    configurator = Chat.EndpointConfigurator.class
)
public class Chat {

    private static final Logger LOG = Logger.getLogger(Chat.class.getName());
    private static final String PRINCIPAL_KEY = "principal";
    private static final String HTTP_SESSION_KEY = "httpSession";

    @OnOpen
    public void onOpen(Session session) {
        // Get principal from user properties (set during handshake)
        Principal principal = (Principal) session.getUserProperties().get(PRINCIPAL_KEY);

        LOG.info("=== OnOpen Debug ===");
        LOG.info("Session ID: " + session.getId());
        LOG.info("Principal from properties: " + (principal != null ? principal.getName() : "NULL"));
        LOG.info("User properties: " + session.getUserProperties());

        if (principal == null) {
            LOG.severe("WebSocket opened without authenticated principal — closing");
            try {
                session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "Authentication required"
                ));
            } catch (IOException e) {
                LOG.severe("Error closing unauthenticated session: " + e.getMessage());
            }
            return;
        }

        LOG.info("✓ WS OPEN for authenticated user: " + principal.getName());

        // TODO: Add user to chat room, etc.
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        Principal principal = (Principal) session.getUserProperties().get(PRINCIPAL_KEY);

        if (principal == null) {
            LOG.warning("Message received without principal — ignoring");
            return;
        }

        LOG.info("Message from " + principal.getName() + ": " + message);

        // TODO: Route message, persist, broadcast
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        Principal principal = (Principal) session.getUserProperties().get(PRINCIPAL_KEY);
        String user = (principal != null) ? principal.getName() : "unknown";

        LOG.info("WS CLOSED for user: " + user + ", reason: " + reason);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        Principal principal = (Principal) session.getUserProperties().get(PRINCIPAL_KEY);
        String user = (principal != null) ? principal.getName() : "unknown";

        LOG.severe("WS ERROR for user: " + user + ", error: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {

        private static final Logger LOG = Logger.getLogger(EndpointConfigurator.class.getName());

        @Override
        public void modifyHandshake(
            ServerEndpointConfig config,
            HandshakeRequest request,
            HandshakeResponse response
        ) {
            LOG.info("=== WebSocket Handshake Started ===");

            // Log all headers
            LOG.info("Request Headers: " + request.getHeaders());
            LOG.info("Request URI: " + request.getRequestURI());

            // Try to get HTTP session
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            if (httpSession != null) {
                LOG.info("✓ HTTP Session found: " + httpSession.getId());
                config.getUserProperties().put(HTTP_SESSION_KEY, httpSession.getId());

                // Log session attributes for debugging
                LOG.info("Session attributes: " + httpSession.getAttributeNames());
            } else {
                LOG.warning("✗ No HTTP session during handshake");
            }

            // Try to get principal from handshake
            Principal principal = request.getUserPrincipal();

            if (principal != null) {
                LOG.info("✓ Principal found: " + principal.getName());
                config.getUserProperties().put(PRINCIPAL_KEY, principal);
            } else {
                LOG.warning("✗ No principal in handshake request");

                // Check for JSESSIONID cookie
                if (request.getHeaders().containsKey("cookie")) {
                    LOG.info("Cookies present: " + request.getHeaders().get("cookie"));
                } else {
                    LOG.warning("No cookies in request!");
                }
            }

            LOG.info("=================================");
        }
    }
}

















// package com.app.corechat.ws;
// import java.io.IOException;
// import java.security.Principal;
// import java.util.logging.Logger;

// import jakarta.annotation.security.DeclareRoles;
// import jakarta.annotation.security.RolesAllowed;
// import jakarta.websocket.HandshakeResponse;
// import jakarta.websocket.OnClose;
// import jakarta.websocket.OnMessage;
// import jakarta.websocket.OnOpen;
// import jakarta.websocket.Session;
// import jakarta.websocket.server.HandshakeRequest;
// import jakarta.websocket.server.ServerEndpoint;
// import jakarta.websocket.server.ServerEndpointConfig;



// @DeclareRoles("USER")
// @RolesAllowed("USER")
// @ServerEndpoint(
//    value =  "/chat",
//         configurator = Chat.EndpointConfigurator.class)
// public class Chat {

//     private static final Logger LOG = Logger.getLogger(Chat.class.getName());


//     @OnOpen
//     public void OnOpen(Session session) {

//         LOG.info("Khesrow says Incomming HTTP handshake on open ");
//         Principal principal = session.getUserPrincipal();

//         if (principal == null) {
//             try{
//             session.close();
//             return;
//         } catch (IOException e) {
                
//             }

//         }

    

//         LOG.info("WS OPEN " + principal.getName());

//     }
    


    

//     @OnMessage
//     public void OnMessage(String message, Session session) {

//         LOG.info("onMEssage's Message: " + message);
//     }
    
    
//     @OnClose
//     public void OnClose(Session session) {

        
            
//             LOG.info("Session Closed " + session.getId());

       
//     }
    

//     public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {

//         @Override
//         public void modifyHandshake(
//             ServerEndpointConfig config,
//             HandshakeRequest request,
//             HandshakeResponse response
//         ) {


//             LOG.info("KHESROW SAYS WS HANDSHAKE STARTED");

//             LOG.info("Bingo Headers are: " + request.getHeaders());

//         //     Object httpSession = request.getHttpSession();

//         //     LOG.info("HTTP Session is: " + httpSession);
//         //      Principal principal = null;

//         //  try {
//         //     principal = request.getUserPrincipal();
                
//         // } catch (Exception e) {
//         //     LOG.warning("PennyWise warning failed to get principal");

//         // }
//             // if (principal != null) {
//             //     LOG.info("principle was not null " + principal.getName());
          
                
//             //     config.getUserProperties().put("principal", principal);
//             // } else {
//             //     LOG.warning("No prinicpal found during handshake");
//             // }


//         }
        
//     }
    


    
    
// }
