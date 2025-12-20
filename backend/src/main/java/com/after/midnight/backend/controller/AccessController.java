package com.after.midnight.backend.controller;

import com.after.midnight.backend.repository.AccessTokenRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class AccessController {

    private final AccessTokenRepository repository;

    public AccessController(AccessTokenRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/validate")
    public boolean validate(@RequestParam String token) {
        return repository.findByTokenAndActiveTrue(token).isPresent();
    }
}
