package com.after.midnight.backend.controller;

import com.after.midnight.backend.model.MusicTrack;
import com.after.midnight.backend.repository.MusicTrackRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
@CrossOrigin(origins = "*")
public class MusicController {

    private final MusicTrackRepository musicRepository;

    public MusicController(MusicTrackRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @GetMapping
    public List<MusicTrack> getAllTracks() {
        return musicRepository.findAll();
    }
}
