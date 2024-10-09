package org.example.output;

import org.junit.jupiter.api.Test;

public class AudioPlayLocalTest {
    @Test
    public void playAudioTest() {
        AudioPlayLocal audioPlayLocal = new AudioPlayLocal();
        audioPlayLocal.play("/Users/ywang77/workspace/java/butler/RecordedAudio2.wav");
    }
}
