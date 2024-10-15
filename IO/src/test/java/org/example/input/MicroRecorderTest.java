package org.example.input;

import org.example.exception.AudioException;
import org.example.input.micro.MicroRecorder;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.*;

public class MicroRecorderTest {

    @Test
    void recordAudioTest() throws InterruptedException {
        MicroRecorder recorder = new MicroRecorder(null, null);
        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new AudioException(MicroRecorderTest.class, "Error stopping recording", e);
            }
            recorder.stopRecording();
        });
        stopper.start();
        recorder.startLocalRecordAndSaveInFile("outputTest.wav");
        Thread.sleep(5000);
    }

    // start record and play in real time. don't test directly in the open sound.
    @Deprecated
    @Test
    void recordAudioStreamingTest() throws InterruptedException, LineUnavailableException {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);

        int BUFFER_SIZE = 1024;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(format, BUFFER_SIZE);

        info = new DataLine.Info(TargetDataLine.class, format);
        final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
        targetLine.open();

        byte[] data = new byte[BUFFER_SIZE];

        sourceLine.start();
        targetLine.start();

        Thread thread = new Thread() {
            @Override
            public void run() {
                int readBytes = -1;
                while ((readBytes = targetLine.read(data, 0, data.length)) != -1) {
                    sourceLine.write(data, 0, readBytes);
                }
            }
        };
        thread.start();
        Thread.sleep(50000);
    }
}
