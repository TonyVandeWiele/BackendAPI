package com.hepl.backendapi.controller.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggingController {

    private static final Logger logger = LoggerFactory.getLogger(LoggingController.class);

    @GetMapping("/log-test")
    public String logTest() {
        logger.info("Info log");
        logger.debug("Debug log");
        logger.error("Error log");
        return "Check your logs!";
    }
}