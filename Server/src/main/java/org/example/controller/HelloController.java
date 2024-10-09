package org.example.controller;

import org.example.event.action.ActionEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    ActionEventPublisher eventPublisher;

    @RequestMapping("/hello")
    String home() {
        eventPublisher.publishEvent("Hello World!");
        return "Hello World!";
    }


}
