package org.example.event.action.audio;

import org.example.event.ButlerBaseEvent;
import org.example.event.RecordEventData;

public class AudioEvent extends ButlerBaseEvent<RecordEventData> {
    public AudioEvent(RecordEventData data) {
        super(data);
    }
}
