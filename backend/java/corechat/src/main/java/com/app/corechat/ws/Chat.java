package com.app.corechat.ws;




import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;





@ServerEndpoint(
   value =  "/chat",
    configurator = Chat.EndpointConfigurator.class)
public class Chat {

    private static final Logger LOG = Logger.getLogger(Chat.class.getName());


    @OnOpen
    public void OnOpen(Session session) {

        LOG.info("Khesrow says Incomming HTTP handshake on open ");
        Principal principal = (Principal) session.getUserProperties().get("principal");

        if (principal == null) {
            try{
            session.close();
            return;
        } catch (IOException e) {
                
            }

        }

    

        LOG.info("WS OPEN " + principal.getName());

    }
    


    

    @OnMessage
    public void OnMessage(String message, Session session) {

        LOG.info("onMEssage's Message: " + message);
    }
    
    
    @OnClose
    public void OnClose(Session session) {

        
            
            LOG.info("Session Closed " + session.getId());

       
    }
    

    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {

        @Override
        public void modifyHandshake(
            ServerEndpointConfig config,
            HandshakeRequest request,
            HandshakeResponse response
        ) {


            LOG.info("KHESROW SAYS WS HANDSHAKE STARTED");

            LOG.info("Bingo Headers are: " + request.getHeaders());

            Object httpSession = request.getHttpSession();

            LOG.info("HTTP Session is: " + httpSession);
             Principal principal = null;

         try {
            principal = request.getUserPrincipal();
                
        } catch (Exception e) {
            LOG.warning("PennyWise warning failed to get principal");
                
        }
            



            

            if (principal != null) {
                LOG.info("principle was not null " + principal.getName());
          
                
                config.getUserProperties().put("principal", principal);
            } else {
                LOG.warning("No prinicpal found during handshake");
            }


        }
        
    }
    


    
    
}
