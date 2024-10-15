package org.example.controller.websocket;

import lombok.extern.slf4j.Slf4j;
import org.example.output.WavFileWriter;
import org.example.registery.PipedStreamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.*;
import java.util.Arrays;

import static org.example.CommonLabel.PipedReigstryLabel.MIC_REC_STREAM;

@Slf4j
@Component
public class AudioWebSocketHandler extends BinaryWebSocketHandler {

    private ByteArrayOutputStream byteBuffer;

    private PipedStreamRegistry pipedStreamRegistry;

    @Autowired
    public AudioWebSocketHandler(PipedStreamRegistry pipedStreamRegistry) {
        this.pipedStreamRegistry = pipedStreamRegistry;
    }

    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
//        log.info("Received audio message{}", Arrays.toString(message.getPayload().array()));
        try {
            byte[] data = message.getPayload().array();
            byteBuffer.write(Arrays.copyOf(data, data.length));
            if (pipedStreamRegistry.get(MIC_REC_STREAM) != null) {
                pipedStreamRegistry.get(MIC_REC_STREAM).getQueueOutputStream().write(data);
            }
        } catch (IOException e) {
            log.error("Error writing audio message to buffer: {}", e.getMessage());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 处理接收到的WebSocket消息
        String payload = message.getPayload();
        log.info("接收到消息：{}", payload);

        // 发送回复消息给客户端
        try {
            session.sendMessage(new TextMessage("from client : "+payload));
        } catch (IOException e) {
            log.error("Error sending message to client: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("Connection established");
        byteBuffer = new ByteArrayOutputStream();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        log.info("Connection closed");
        // TODO to remove
        WavFileWriter.writeToWavFile("output2.wav", byteBuffer.toByteArray());
        byteBuffer.close();
        if (pipedStreamRegistry != null && pipedStreamRegistry.get(MIC_REC_STREAM) != null) {
            pipedStreamRegistry.get(MIC_REC_STREAM).getQueueOutputStream().close();
        }
    }
}
