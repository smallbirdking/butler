package org.example.event;

import lombok.Getter;

@Getter
public class RecordEventData extends ButlerBaseEventData {

    private final EventType EVENT_TYPE = EventType.RECORD;
    private RecordCommandType command;

    private String message;

    public RecordEventData(RecordCommandType commandType) {
        this(commandType, null);
    }

    public RecordEventData(RecordCommandType commandType, String message) {
        this.message = message;
        this.command = commandType;
    }

    @Override
    public EventType getEventType() {
        return EVENT_TYPE;
    }

    public enum RecordCommandType {

        START("START"),
        STOP("STOP"),
        STREAM_START("STREAM_START"),
        STREAM_STOP("STREAM_STOP");

        private final String command;

        RecordCommandType(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }
}
