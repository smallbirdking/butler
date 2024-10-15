package org.example.controller;

import org.example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/audio/")
public class AudioController {
    @Autowired
    RecordService recordService;

    @PostMapping("/record")
    String processRecord(@RequestParam String command) {
        return recordService.processRecord(command);
    }

    @GetMapping("/record/status")
    String checkAudioStatus() {
        return recordService.checkMicroStatus();
    }

    @GetMapping("/record/stream/status")
    String checkStreamStatus() {
        return recordService.checkStreamStatus();
    }

    @PostMapping("/record/stream")
    String recordStream(@RequestParam String command) {
        return recordService.recordStream(command);
    }
}
