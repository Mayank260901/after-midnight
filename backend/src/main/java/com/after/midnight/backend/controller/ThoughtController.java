package com.after.midnight.backend.controller;

import com.after.midnight.backend.model.Thought;
import com.after.midnight.backend.repository.ThoughtRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thoughts")
@CrossOrigin(origins = "*")
public class ThoughtController {

    private final ThoughtRepository thoughtRepository;

    public ThoughtController(ThoughtRepository thoughtRepository) {
        this.thoughtRepository = thoughtRepository;
    }

    @GetMapping
    public List<Thought> getAllThoughts() {
        return thoughtRepository.findAll();
    }
}
