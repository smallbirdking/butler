package org.example.util;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import javax.sound.sampled.AudioFormat;
import java.io.File;
import java.io.IOException;

@Slf4j
public class AudioUtil {

    private static String FFMPEG_PATH = "/opt/homebrew/bin/ffmpeg";

    private AudioUtil() {
        // Utility class
    }

    public static AudioFormat getAudioFormat() {
        return getAudioFormat(false);
    }

    public static AudioFormat getAudioFormat(boolean bigEndian) {
        return new AudioFormat(44100, 16, 1, true, bigEndian);
    }

    public static void wavToMap3Converter(String wavFile, String mp3File) throws IOException {
        // convert wav to mp3
        File output = new File(mp3File);
        FFmpeg ffmpeg = new FFmpeg(FFMPEG_PATH);
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(wavFile)
                .overrideOutputFiles(true)
                .addOutput(output.getAbsolutePath())
                .setFormat("mp2")
                .setAudioCodec("libmp3lame")
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
        executor.createJob(builder).run();
        log.info("Generated mp3 file: " + output.getAbsolutePath());
    }
}
