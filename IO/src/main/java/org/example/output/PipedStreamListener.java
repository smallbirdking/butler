package org.example.output;

import org.example.registery.StreamingPipe;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class PipedStreamListener {
    private StreamingPipe pipe;
    private AtomicBoolean swich;
    private ByteArrayOutputStream byteBuffer;

    public PipedStreamListener(StreamingPipe pipe) {
        this.pipe = pipe;
        swich = new AtomicBoolean(false);
        byteBuffer = new ByteArrayOutputStream();
    }

    public void listen() {
        swich.set(true);
        Thread thread = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while (swich.get() && (bytesRead = pipe.getInputStream().read(buffer)) != -1) {
                    byteBuffer.write(Arrays.copyOf(buffer, bytesRead));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void stop() {
        try {
            swich.set(false);
            WavFileWriter.writeToWavFile("output.wav", byteBuffer.toByteArray());
            byteBuffer.close();
            pipe.getInputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
