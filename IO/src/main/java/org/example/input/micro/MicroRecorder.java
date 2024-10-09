package org.example.input.micro;

import lombok.extern.slf4j.Slf4j;
import org.example.registery.IResourceRegistry;
import org.example.registery.StreamingPipe;
import org.example.util.AudioUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class MicroRecorder implements IResourceRegistry {
    public static final String LABEL = "MIC_REC_LOCAL";
    private AudioFormat format;
    private TargetDataLine line;
    private AudioInputStream audioInputStream;
    private Thread recordingThread;

    public MicroRecorder() {
        this.format = AudioUtil.getAudioFormat(true);
    }

    public void startLocalRecordAndSaveInFile(String fileName) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            log.warn("Line not supported");
            return;
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            audioInputStream = new AudioInputStream(line);
            recordingThread = new Thread(() -> {
                try {
                    File outputFile = new File(fileName);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
                } catch (IOException e) {
                    log.error("Error recording audio", e);
                }
            });
            recordingThread.start();
            log.info("Recording started...");
        } catch (LineUnavailableException e) {
            log.error("Record failure : " , e);
        }
    }

    public void startLocalRecordToPipedStream(StreamingPipe pipe) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            log.warn("Line not supported");
            return;
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            audioInputStream = new AudioInputStream(line);
            byte[] buffer = new byte[1024];
            recordingThread = new Thread(() -> {
                while (line.isOpen()) {
                    try {
                        int readBytes = line.read(buffer, 0, buffer.length);
                        if (readBytes > 0) {
                            pipe.getOutputStream().write(buffer);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            recordingThread.start();
            log.info("Recording started...");
        } catch (LineUnavailableException e) {
            log.error("Record failure : " , e);
        }
    }

    public void startRecordToRemoteStreaming(WebSocketSession session) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            log.warn("Line not supported");
            return;
        }
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            byte[] buffer = new byte[1024];
            recordingThread = new Thread(() -> {
                while (line.isOpen()) {
                    try {
                        int readBytes = line.read(buffer, 0, buffer.length);
                        if (readBytes > 0) {
                            session.sendMessage(new BinaryMessage(buffer));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            recordingThread.start();
            log.info("Recording started...");
        } catch (LineUnavailableException e) {
            log.error("Record failure : " , e);
        }
    }

    public boolean isRecording() {
        return line != null && line.isActive();
    }

    public void stopRecording() {
        if (line != null) {
            line.stop();
            line.close();
            log.info("Recording stopped.");
        }
        if (recordingThread != null) {
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                log.error("Error stopping recording", e);
            }
        }
    }

    @Override
    public String getLabel() {
        return LABEL;
    }

}
