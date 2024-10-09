package org.example.event.action.audio;

import lombok.extern.slf4j.Slf4j;
import org.example.input.micro.MicroRecorder;
import org.example.output.PipedStreamListener;
import org.example.registery.PipedStreamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordEventListener {

    private MicroRecorder recorder;
    private PipedStreamRegistry pipedStreamRegistry;

    @Autowired
    public RecordEventListener(MicroRecorder recorder, PipedStreamRegistry pipedStreamRegistry) {
        this.recorder = recorder;
        this.pipedStreamRegistry = pipedStreamRegistry;
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
        recorder.startLocalRecordToPipedStream(pipedStreamRegistry.get("MIC_REC_STREAM"));
    }

    public void stopRecord() {
        recorder.stopRecording();
    }

    public void startStream() {
        PipedStreamListener listener = new PipedStreamListener(pipedStreamRegistry.get("MIC_REC_STREAM"));
        listener.listen();
    }

    public void stopStream() {
        PipedStreamListener listener = new PipedStreamListener(pipedStreamRegistry.get("MIC_REC_STREAM"));
        listener.stop();
    }
}
