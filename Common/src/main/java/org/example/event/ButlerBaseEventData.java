package org.example.event;

import org.springframework.context.ApplicationEvent;

public abstract class ButlerBaseEventData {

    private EventType eventType;

    public ButlerBaseEventData() {
        eventType = getEventType();
    }

    public abstract EventType getEventType();

}
