package com.app.corechat.ws;

import java.util.logging.Logger;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class Chat {

    private static final Logger LOG = Logger.getLogger(Chat.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        LOG.info("🔵 WS OPEN - Session ID: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("📨 WS MESSAGE - Session: " + session.getId() + ", Message: " + message);

        // Echo back for now
        try {
            session.getBasicRemote().sendText("Server received: " + message);
        } catch (Exception e) {
            LOG.severe("Error sending message: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOG.info("🔴 WS CLOSED - Session ID: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOG.severe("❌ WS ERROR - Session: " + session.getId() + ", Error: " + error.getMessage());
    }
}
