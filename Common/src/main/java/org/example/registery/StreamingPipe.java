package org.example.registery;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.QueueInputStream;
import org.apache.commons.io.output.QueueOutputStream;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

@Slf4j
@Getter
public class StreamingPipe {
//    PipedOutputStream outputStream;
//    PipedInputStream inputStream;

    QueueInputStream queueInputStream;
    QueueOutputStream queueOutputStream;

    public StreamingPipe() {
        try {
//            outputStream = new PipedOutputStream();
//            inputStream = new PipedInputStream(outputStream);
            queueInputStream = new QueueInputStream();
            queueOutputStream = queueInputStream.newQueueOutputStream();
        } catch (Exception e) {
            log.error("Error creating pipe", e);
        }
    }
}
