package org.example.controller;

import java.util.*;

import org.example.*;
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

    @Autowired
    private SpeechRepository speechRepository;

    @RequestMapping("/mongodb/findAll")
    String mongodb() {
        List<Speech> all = speechRepository.findAll();
        return Arrays.toString(all.toArray());
    }


}
