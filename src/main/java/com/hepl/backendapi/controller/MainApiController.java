package com.hepl.backendapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainApiController {
    @GetMapping("/test")
    public String test() {
        return "Spring Boot Web Dependency Test Successful!";
    }
}
