package com.app.corechat.filter;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class WebSocketConfigurator extends ServerEndpointConfig.Configurator {
    

    
    
    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response) {

        // Allow all origins for development
        response.getHeaders().put("Access-Control-Allow-Origin",
            java.util.Collections.singletonList("*"));
        response.getHeaders().put("Access-Control-Allow-Credentials",
            java.util.Collections.singletonList("true"));

        super.modifyHandshake(config, request, response);
    }

}
