package org.example.input;

import org.example.input.micro.MicroRecorder;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.*;

public class MicroRecorderTest {

    @Test
    void recordAudioTest() throws InterruptedException {
        MicroRecorder recorder = new MicroRecorder();
        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            recorder.stopRecording();
        });
        stopper.start();
        recorder.startLocalRecordAndSaveInFile("outputTest.wav");
        Thread.sleep(5000);
    }

    @Test
    void recordAudioStreamingTest() throws InterruptedException, LineUnavailableException {
        AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        final SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(format, 1024);

        info = new DataLine.Info(TargetDataLine.class, format);
        final TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
        targetLine.open();

        int BUFFER_SIZE = 1024;
        byte[] data = new byte[BUFFER_SIZE];

        sourceLine.start();
        targetLine.start();

        Thread thread = new Thread() {
            @Override
            public void run() {
//                while (true) {
//                    targetLine.read(data, 0, data.length);
//                    System.out.println(Arrays.toString(data));
                    int readBytes = -1;
                    while ((readBytes = targetLine.read(data, 0, data.length)) != -1) {
                        sourceLine.write(data, 0, readBytes);
                    }
//                    sourceLine.write(data, 0, data.length);
//                    byte counter = 0;
//                    byte sign = 1;
//                    int threshold = (int) format.getFrameRate();
//                    for (int i = 0; i < 1024; i++) {
//                        if (counter > threshold) {
//                            sign = (byte) -sign;
//                            counter = 0;
//                        }
//                        data[i] = (byte) (sign * 30);
//                        counter++;
//                    }
//                    // the next call is blocking until the entire buffer is
//                    // sent to the SourceDataLine
//                    sourceLine.write(data, 0, 1024);
                }
//            }
        };


        thread.start();
        Thread.sleep(50000);
    }
}
