package org.example.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ButlerBaseEvent<T>  extends ApplicationEvent {

        private T data;

        public ButlerBaseEvent(T data) {
            super(data);
            this.data = data;
        }
}
