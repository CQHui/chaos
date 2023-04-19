package com.qihui.chaos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChaosController {

    @GetMapping
    @RequestMapping(value = "/slowAPI")
    public ResponseEntity<String> slowResponseTIme(@RequestParam("timeSlowness") int slowness) throws InterruptedException {
        Thread.sleep(slowness);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/thread")
    String getThreadName() {
        return Thread.currentThread().toString();
    }
}
