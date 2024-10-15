package org.example.input.micro;

import lombok.extern.slf4j.Slf4j;
import org.example.output.PipedStreamHandler;
import org.example.registery.*;
import org.example.util.AudioUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.sound.sampled.*;

import java.io.*;
import java.util.Optional;

import static org.example.CommonLabel.PipedReigstryLabel.*;


@Slf4j
@Component
public class MicroRecorder implements IResourceRegistry {

    public static final String LABEL = LOCAL_MICRO;

    private AudioFormat format;
    private TargetDataLine line;
    private AudioInputStream audioInputStream;
    private Thread recordingThread;
    private PipedStreamRegistry pipedStreamRegistry;
    private PipedStreamHandler pipedStreamHandler;

    public MicroRecorder(PipedStreamRegistry pipedStreamRegistry, PipedStreamHandler pipedStreamHandler) {
        this.format = AudioUtil.getAudioFormat(true);
        this.pipedStreamRegistry = pipedStreamRegistry;
        this.pipedStreamHandler = pipedStreamHandler;
    }

    public void startLocalRecordAndSaveInFile(String fileName) {
        basicStartRecord(getSaveFileThread(fileName));
    }

    public void startLocalRecordToPipedStream() {
        basicStartRecord(getPipedStreamThread());
    }

    public void startRecordToRemoteStreaming(WebSocketSession session) {
        basicStartRecord(getRemoteStreamingThread(session));
    }

    public boolean isRecording() {
        return line != null && line.isActive();
    }

    public void stopRecording() {
        if (pipedStreamRegistry != null && pipedStreamRegistry.get(MIC_REC_STREAM) != null) {
            pipedStreamHandler.stop();
        }
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

    void basicStartRecord(Runnable recordRunnable) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            log.warn("Line not supported");
            return;
        }
        try {
            if (line != null && line.isOpen()) {
                log.info("Recording already started...");
                return;
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            recordingThread = new Thread(recordRunnable);
            recordingThread.start();
            log.info("Recording started...");
        } catch (LineUnavailableException e) {
            log.error("Record failure : ", e);
        }
    }

    private Runnable getSaveFileThread(String fileName) {
        return () -> {
            audioInputStream = new AudioInputStream(line);
            try {
                File outputFile = new File(fileName);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
            } catch (IOException e) {
                log.error("Error recording audio", e);
            }
        };
    }

    private Runnable getPipedStreamThread() {
        return () -> {
            byte[] buffer = new byte[1024];
            while (line.isOpen()) {
                int readBytes = line.read(buffer, 0, buffer.length);
                if (readBytes > 0) {
                    Optional.ofNullable(pipedStreamRegistry.get(MIC_REC_STREAM)).ifPresent(pipe -> {
                        try {
                            pipe.getQueueOutputStream().write(buffer);
                        } catch (IOException e) {
                            log.error("Error writing to pipe", e);
                        }
                    });
                }
            }
        };
    }

    private Runnable getRemoteStreamingThread(WebSocketSession session) {
        return () -> {
            byte[] buffer = new byte[1024];
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
        };
    }
}
