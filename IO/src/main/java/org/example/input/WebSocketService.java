package org.example.input;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
public class WebSocketService extends TextWebSocketHandler {

    private WebSocketSession session;

    private WebSocketClient client = new StandardWebSocketClient();

    public void connect() {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, this, "ws://localhost:8080/audio/stream");
        manager.setAutoStartup(true);
        manager.start();
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void disconnect() throws IOException {
        session.close();
    }

    public boolean isConnected() {
        return session != null;
    }

    public void send(String message) throws IOException {
        if (session == null) {
            throw new IllegalStateException("Not connected");
        }
        session.sendMessage(new TextMessage(message));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.session = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Received message: {}", message.getPayload());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.info("Received binary message{}", message.getPayload());
    }

}
