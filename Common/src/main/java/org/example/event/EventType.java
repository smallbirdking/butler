package org.example.event;

public enum EventType {
    RECORD("RECORD");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
