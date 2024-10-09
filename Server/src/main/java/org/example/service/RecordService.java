package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.event.RecordEventData;
import org.example.event.action.ActionEventPublisher;
import org.example.event.action.audio.AudioEvent;
import org.example.input.micro.MicroRecorder;
import org.example.registery.CommonResourceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class RecordService {

    private ActionEventPublisher eventPublisher;
    private CommonResourceRegistry registry;
    private final String AUDIO_RECORD_ACTION_START;
    private final String AUDIO_RECORD_ACTION_STOP;

    @Autowired
    public RecordService(ActionEventPublisher eventPublisher, CommonResourceRegistry registry,
                         @Value("${audio.record.action.start}") String audioRecordActionStart,
                         @Value("${audio.record.action.stop}") String audioRecordActionStop) {
        this.eventPublisher = eventPublisher;
        this.registry = registry;
        this.AUDIO_RECORD_ACTION_START = audioRecordActionStart;
        this.AUDIO_RECORD_ACTION_STOP = audioRecordActionStop;
    }

    public String processRecord(String command) {
        RecordEventData.RecordCommandType commandType = getRecordCommandType(command);
        if (commandType != null) {
            eventPublisher.publishEvent(new AudioEvent(new RecordEventData(commandType)));
        }
        return String.format("Audio %sing...", command);
    }

    public String checkMicroStatus() {
        if (((MicroRecorder) registry.get(MicroRecorder.LABEL)).isRecording()) {
            return "Micro is recording";
        } else {
            return "Micro is not recording";
        }
    }

    public String recordStream(String command) {
        RecordEventData.RecordCommandType commandType = getStreamRecordCommandType(command);
        Optional.ofNullable(commandType).ifPresent(ct ->
                eventPublisher.publishEvent(new AudioEvent(new RecordEventData(ct))));
        return String.format("Streaming audio %sing...", command);
    }


    private RecordEventData.RecordCommandType getRecordCommandType(String command) {
        RecordEventData.RecordCommandType commandType = null;
        if (AUDIO_RECORD_ACTION_START.equals(command)) {
            commandType = RecordEventData.RecordCommandType.START;
        } else if (AUDIO_RECORD_ACTION_STOP.equals(command)) {
            commandType = RecordEventData.RecordCommandType.STOP;
        } else {
            log.error("Invalid command");
        }
        return commandType;
    }

    private RecordEventData.RecordCommandType getStreamRecordCommandType(String command) {
        RecordEventData.RecordCommandType commandType = null;
        if (AUDIO_RECORD_ACTION_START.equals(command)) {
            commandType = RecordEventData.RecordCommandType.STREAM_START;
        } else if (AUDIO_RECORD_ACTION_STOP.equals(command)) {
            commandType = RecordEventData.RecordCommandType.STREAM_STOP;
        } else {
            log.error("Invalid command");
        }
        return commandType;
    }
}
