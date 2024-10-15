package org.example.output;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.QueueInputStream;
import org.example.input.micro.MicroRecorder;
import org.example.registery.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.CommonLabel.PipedReigstryLabel.MIC_REC_STREAM;

@Slf4j
@Component
public class PipedStreamHandler {
    private final AtomicBoolean swich;
    private ByteArrayOutputStream byteBuffer;
    private Thread thread;
    private final PipedStreamRegistry pipedStreamRegistry;
    private final CommonResourceRegistry commonResourceRegistry;

    public PipedStreamHandler(PipedStreamRegistry pipedStreamRegistry, CommonResourceRegistry commonResourceRegistry) {
        swich = new AtomicBoolean(false);
        this.pipedStreamRegistry = pipedStreamRegistry;
        this.commonResourceRegistry = commonResourceRegistry;
    }

    public void listen() {
        if (thread != null && thread.isAlive()) {
            log.info("Thread already started");
            return;
        }
        if (isMicoOn()) {
            log.info("MicroRecorder not started");
            return;
        }
        swich.set(true);
        byteBuffer = new ByteArrayOutputStream();
        pipedStreamRegistry.register(MIC_REC_STREAM, new StreamingPipe());
        log.info("Starting stream");
        thread = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                QueueInputStream inputStream = this.pipedStreamRegistry.get(MIC_REC_STREAM).getQueueInputStream();
                while (swich.get() && byteBuffer != null && pipedStreamRegistry.get(MIC_REC_STREAM) != null) {
                    bytesRead = inputStream.read(buffer);
                    if (bytesRead > 0) {
                        byteBuffer.write(Arrays.copyOf(buffer, bytesRead));
                    }
                }
                log.info("listener stopped");
            } catch (IOException e) {
                log.error("Error listening to stream", e);
            }
        });
        thread.start();
    }

    private boolean isMicoOn() {
        return commonResourceRegistry.get(MicroRecorder.LABEL) == null || !((MicroRecorder) commonResourceRegistry.get(MicroRecorder.LABEL)).isRecording();
    }


    public void stop() {
        try {
            StreamingPipe streamingPipe = pipedStreamRegistry.get(MIC_REC_STREAM);
            if (streamingPipe == null) {
                log.info("Stream not started yet");
                return;
            }

            if (byteBuffer != null && byteBuffer.size() > 0) {
                log.info("Writing to file");
                WavFileWriter.writeToWavFile("output.wav", byteBuffer.toByteArray());
                byteBuffer.close();
                byteBuffer = null;
            }

            pipedStreamRegistry.unregister(MIC_REC_STREAM);

            swich.set(false);
            if (thread != null) {
                thread.join();
            }

            streamingPipe.getQueueInputStream().close();
            log.info("Stream stopped");
        } catch (IOException | InterruptedException e) {
            log.error("Error stopping stream", e);
        }
    }
}
