package com.aftermidnight.service.impl;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.ThoughtRepository;
import com.aftermidnight.service.ThoughtService;
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
public class ThoughtServiceImpl implements ThoughtService {

    private final ThoughtRepository thoughtRepository;

    @Override
    @Transactional
    public Thought create(Thought thought) {
        log.info("Saving new thought for user: {}", thought.getUser().getEmail());
        if (thought.getStatus() == PublicationStatus.PUBLISHED && thought.getPublishedAt() == null) {
            thought.setPublishedAt(LocalDateTime.now());
        }
        return thoughtRepository.save(thought);
    }

    @Override
    public Page<Thought> getAllByUser(User user, Pageable pageable) {
        log.info("Fetching thoughts for user: {} with pageable: {}", user.getEmail(), pageable);
        return thoughtRepository.findByUserAndDeletedFalse(user, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Page<Thought> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable) {
        log.info("Fetching thoughts for user: {} with status: {} and pageable: {}", user.getEmail(), status, pageable);
        return thoughtRepository.findByUserAndStatusAndDeletedFalse(user, status, pageable);
    }

    @Override
    public Page<Thought> getAllPublished(Pageable pageable) {
        log.info("Fetching all published thoughts with pageable: {}", pageable);
        return thoughtRepository.findByStatusAndDeletedFalse(PublicationStatus.PUBLISHED, pageable);
    }

    @Override
    public Optional<Thought> getById(Long id) {
        log.info("Fetching thought by id: {}", id);
        return thoughtRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting thought by id: {}", id);
        thoughtRepository.findByIdAndDeletedFalse(id).ifPresent(thought -> {
            thought.setDeleted(true);
            thoughtRepository.save(thought);
        });
    }

    @Override
    @Transactional
    public Thought updateStatus(Long id, PublicationStatus status) {
        log.info("Updating status for thought id: {} to {}", id, status);
        return thoughtRepository.findByIdAndDeletedFalse(id).map(thought -> {
            thought.setStatus(status);
            if (status == PublicationStatus.PUBLISHED && thought.getPublishedAt() == null) {
                thought.setPublishedAt(LocalDateTime.now());
            }
            return thoughtRepository.save(thought);
        }).orElseThrow(() -> new RuntimeException("Thought not found"));
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        log.info("Incrementing view count for thought id: {}", id);
        thoughtRepository.findByIdAndDeletedFalse(id).ifPresent(thought -> {
            thought.setViewCount(thought.getViewCount() + 1);
            thoughtRepository.save(thought);
        });
    }
}
