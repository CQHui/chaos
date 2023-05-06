package com.qihui.chaos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elliott Chen on 2023/5/6 13:05
 */
@RestController
public class HelloWebFluxController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, WebFlux !";
    }

    @GetMapping("/user")
    public Mono<Map<String, String>> getUser() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("variety", "cat");
        return Mono.just(map);
    }

    @GetMapping
    @RequestMapping(value = "/webflux/slowAPI")
    public Mono<String> slowResponseTIme(@RequestParam("timeSlowness") int slowness) {
        return Mono.just("success")
                .delayElement(Duration.ofMillis(slowness));
    }

    @GetMapping("/webflux/thread")
    public Mono<String> getThreadName() {
        return Mono.just(Thread.currentThread().toString())
                .delayElement(Duration.ofSeconds(1));
    }

}
