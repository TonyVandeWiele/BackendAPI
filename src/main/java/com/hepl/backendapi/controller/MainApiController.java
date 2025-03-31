package com.hepl.backendapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1")
@Tag(name = "Main")
public class MainApiController {

    @Operation(summary = "Home page")
    @GetMapping("/")
    public String home() {
        return "Welcome! This is the home page.";
    }
}
