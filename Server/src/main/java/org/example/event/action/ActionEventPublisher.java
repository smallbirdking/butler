package org.example.event.action;

import lombok.extern.slf4j.Slf4j;
import org.example.event.BaseEventPublisher;
import org.example.event.ButlerBaseEventData;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActionEventPublisher extends BaseEventPublisher {

    public ActionEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        super(applicationEventPublisher);
    }

    public void publishEvent(ButlerBaseEventData event) {
        super.publishEvent(event);
    }
}
