package org.example.registery;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Slf4j
@Getter
public class StreamingPipe {
    PipedOutputStream outputStream;
    PipedInputStream inputStream;

    public StreamingPipe() {
        try {
            outputStream = new PipedOutputStream();
            inputStream = new PipedInputStream(outputStream);
        } catch (Exception e) {
            log.error("Error creating pipe", e);
        }
    }
}
