package org.example.output;

import lombok.extern.slf4j.Slf4j;
import org.example.util.AudioUtil;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class WavFileWriter {

    public static void writeToWavFile(String fileName, byte[] audioBytes) {
        try {
            AudioFormat format = AudioUtil.getAudioFormat(true);
            File wavFile = new File(fileName);
            writeBytesToWavFile(audioBytes, wavFile, format);
            log.info("WAV file written successfully.");
        } catch (IOException e) {
            log.error("Error writing WAV file: " + e.getMessage());
        }
    }

    public static void writeBytesToWavFile(byte[] audioBytes, File wavFile, AudioFormat format) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
             AudioInputStream audioInputStream = new AudioInputStream(bais, format, audioBytes.length / format.getFrameSize())) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
        }
    }
}
