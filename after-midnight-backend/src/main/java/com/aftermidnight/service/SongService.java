package com.aftermidnight.service;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Song;
import com.aftermidnight.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SongService {
    Song create(Song song);
    Page<Song> getAllByUser(User user, Pageable pageable);
    Page<Song> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable);
    Page<Song> getAllPublished(Pageable pageable);
    Optional<Song> getById(Long id);
    void delete(Long id);
    Song updateStatus(Long id, PublicationStatus status);
    void incrementViewCount(Long id);
}
