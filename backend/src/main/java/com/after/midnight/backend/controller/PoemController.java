package com.after.midnight.backend.controller;

import com.after.midnight.backend.model.Poem;
import com.after.midnight.backend.repository.PoemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poems")
@CrossOrigin(origins = "*")
public class PoemController {

    private final PoemRepository poemRepository;

    public PoemController(PoemRepository poemRepository) {
        this.poemRepository = poemRepository;
    }

    @GetMapping
    public List<Poem> getAllPoems() {
        return poemRepository.findAll();
    }
}
