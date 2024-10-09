package org.example.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BaseEventListener {

    @EventListener
    public void processApplicationEvent(ButlerBaseEvent event) {
       log.info("Received event: " + event);
    }
}
