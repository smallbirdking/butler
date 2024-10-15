package org.example.registery;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.QueueInputStream;
import org.apache.commons.io.output.QueueOutputStream;

@Slf4j
@Getter
public class StreamingPipe {
    QueueInputStream queueInputStream;
    QueueOutputStream queueOutputStream;

    public StreamingPipe() {
        queueInputStream = new QueueInputStream();
        queueOutputStream = queueInputStream.newQueueOutputStream();
    }
}
