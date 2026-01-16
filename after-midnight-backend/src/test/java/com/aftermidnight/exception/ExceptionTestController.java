package com.aftermidnight.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestController {
    @GetMapping({"/api/test-exception", "/api/v1/test-exception"})
    public void throwException() {
        throw new RuntimeException("Test exception message");
    }
}