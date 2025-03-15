package com.hepl.backendapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
    @GetMapping("/swagger-example")
    public String test() {
        return "redirect:/index.html";
    }
}
