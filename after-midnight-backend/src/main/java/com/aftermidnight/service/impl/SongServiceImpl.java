package com.aftermidnight.service.impl;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Song;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.SongRepository;
import com.aftermidnight.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    @Override
    @Transactional
    public Song create(Song song) {
        log.info("Saving new song: {}", song.getTitle());
        if (song.getStatus() == PublicationStatus.PUBLISHED && song.getPublishedAt() == null) {
            song.setPublishedAt(LocalDateTime.now());
        }
        return songRepository.save(song);
    }

    @Override
    public Page<Song> getAllByUser(User user, Pageable pageable) {
        log.info("Fetching songs for user: {} with pageable: {}", user.getEmail(), pageable);
        return songRepository.findByUserAndDeletedFalse(user, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Page<Song> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable) {
        log.info("Fetching songs for user: {} with status: {} and pageable: {}", user.getEmail(), status, pageable);
        return songRepository.findByUserAndStatusAndDeletedFalse(user, status, pageable);
    }

    @Override
    public Page<Song> getAllPublished(Pageable pageable) {
        log.info("Fetching all published songs with pageable: {}", pageable);
        return songRepository.findByStatusAndDeletedFalse(PublicationStatus.PUBLISHED, pageable);
    }

    @Override
    public Optional<Song> getById(Long id) {
        log.info("Fetching song by id: {}", id);
        return songRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting song by id: {}", id);
        songRepository.findByIdAndDeletedFalse(id).ifPresent(song -> {
            song.setDeleted(true);
            songRepository.save(song);
        });
    }

    @Override
    @Transactional
    public Song updateStatus(Long id, PublicationStatus status) {
        log.info("Updating status for song id: {} to {}", id, status);
        return songRepository.findByIdAndDeletedFalse(id).map(song -> {
            song.setStatus(status);
            if (status == PublicationStatus.PUBLISHED && song.getPublishedAt() == null) {
                song.setPublishedAt(LocalDateTime.now());
            }
            return songRepository.save(song);
        }).orElseThrow(() -> new RuntimeException("Song not found"));
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        log.info("Incrementing view count for song id: {}", id);
        songRepository.findByIdAndDeletedFalse(id).ifPresent(song -> {
            song.setViewCount(song.getViewCount() + 1);
            songRepository.save(song);
        });
    }
}
