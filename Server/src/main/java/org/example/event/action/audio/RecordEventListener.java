package org.example.event.action.audio;

import lombok.extern.slf4j.Slf4j;
import org.example.input.micro.MicroRecorder;
import org.example.output.PipedStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordEventListener {

    private MicroRecorder recorder;
    private PipedStreamHandler handler;

    @Autowired
    public RecordEventListener(MicroRecorder recorder, PipedStreamHandler handler) {
        this.recorder = recorder;
        this.handler = handler;
    }

    @EventListener(value = AudioEvent.class,
            condition = "#event.data.eventType == T(org.example.event.EventType).RECORD")
    public void processRecordEvent(AudioEvent event) {
        log.info("Received event: {}", event);
        switch (event.getData().getCommand()) {
            case START:
                startRecord();
                break;
            case STOP:
                stopRecord();
                break;
            case STREAM_START:
                startStream();
                break;
            case STREAM_STOP:
                stopStream();
                break;
            default:
                break;
        }
    }

    public void startRecord() {
//        recorder.startLocalRecordAndSaveInFile("recorded.wav");
        recorder.startLocalRecordToPipedStream();
    }

    public void stopRecord() {
        recorder.stopRecording();
    }

    public void startStream() {
        handler.listen();
    }

    public void stopStream() {
        handler.stop();
    }
}
