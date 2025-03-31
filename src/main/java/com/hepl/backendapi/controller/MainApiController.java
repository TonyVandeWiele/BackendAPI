package com.hepl.backendapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
