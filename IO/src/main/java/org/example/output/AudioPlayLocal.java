package org.example.output;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

@Slf4j
public class AudioPlayLocal {

    public void play(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            log.info("Playing audio locally");
            clip.drain();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            log.error("Error playing audio", e);
        }
    }
}
