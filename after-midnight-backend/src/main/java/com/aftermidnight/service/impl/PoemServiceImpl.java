package com.aftermidnight.service.impl;

import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import com.aftermidnight.repository.PoemRepository;
import com.aftermidnight.service.PoemService;
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
public class PoemServiceImpl implements PoemService {

    private final PoemRepository poemRepository;

    @Override
    @Transactional
    public Poem create(Poem poem) {
        log.info("Saving new poem: {}", poem.getTitle());
        if (poem.getStatus() == PublicationStatus.PUBLISHED && poem.getPublishedAt() == null) {
            poem.setPublishedAt(LocalDateTime.now());
        }
        return poemRepository.save(poem);
    }

    @Override
    public Page<Poem> getAllByUser(User user, Pageable pageable) {
        log.info("Fetching poems for user: {} with pageable: {}", user.getEmail(), pageable);
        return poemRepository.findByUserAndDeletedFalse(user, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
    }

    @Override
    public Page<Poem> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable) {
        log.info("Fetching poems for user: {} with status: {} and pageable: {}", user.getEmail(), status, pageable);
        return poemRepository.findByUserAndStatusAndDeletedFalse(user, status, pageable);
    }

    @Override
    public Page<Poem> getAllPublished(Pageable pageable) {
        log.info("Fetching all published poems with pageable: {}", pageable);
        return poemRepository.findByStatusAndDeletedFalse(PublicationStatus.PUBLISHED, pageable);
    }

    @Override
    public Optional<Poem> getById(Long id) {
        log.info("Fetching poem by id: {}", id);
        return poemRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting poem by id: {}", id);
        poemRepository.findByIdAndDeletedFalse(id).ifPresent(poem -> {
            poem.setDeleted(true);
            poemRepository.save(poem);
        });
    }

    @Override
    @Transactional
    public Poem updateStatus(Long id, PublicationStatus status) {
        log.info("Updating status for poem id: {} to {}", id, status);
        return poemRepository.findByIdAndDeletedFalse(id).map(poem -> {
            poem.setStatus(status);
            if (status == PublicationStatus.PUBLISHED && poem.getPublishedAt() == null) {
                poem.setPublishedAt(LocalDateTime.now());
            }
            return poemRepository.save(poem);
        }).orElseThrow(() -> new RuntimeException("Poem not found"));
    }

    @Override
    @Transactional
    public void incrementViewCount(Long id) {
        log.info("Incrementing view count for poem id: {}", id);
        poemRepository.findByIdAndDeletedFalse(id).ifPresent(poem -> {
            poem.setViewCount(poem.getViewCount() + 1);
            poemRepository.save(poem);
        });
    }
}
