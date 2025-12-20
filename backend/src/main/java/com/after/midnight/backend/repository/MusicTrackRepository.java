package com.after.midnight.backend.repository;

import com.after.midnight.backend.model.MusicTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicTrackRepository extends JpaRepository<MusicTrack, Long> {
}
