package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.input.micro.MicroRecorder;
import org.example.input.WebSocketService;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class VoiceChatSocketTest {

    @Test
    public void testWebsocket() throws InterruptedException {
        WebSocketService webSocketService = new WebSocketService();
        webSocketService.connect();
        Thread.sleep(1000);
        MicroRecorder recorder = new MicroRecorder();
        recorder.startRecordToRemoteStreaming(webSocketService.getSession());

        Thread stopThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                recorder.stopRecording();
                webSocketService.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        stopThread.start();
        stopThread.join();
        log.info("Test finished");
    }


    @Test
    public void testConnection() throws ExecutionException, InterruptedException {
        WebSocketHandler customHandler = new WebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) {
                log.info("Connected to the server!");
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
                log.info("Received : " + message.getPayload());
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) {
                log.error("Error : " + exception.getMessage());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
                log.info("Connection closed!");
            }

            @Override
            public boolean supportsPartialMessages() {
                return false;
            }
        };
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(customHandler, "ws://localhost:8080/audio");
    }

}
