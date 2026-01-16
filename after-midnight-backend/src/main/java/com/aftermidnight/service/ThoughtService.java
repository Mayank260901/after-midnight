package com.aftermidnight.service;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ThoughtService {
    Thought create(Thought thought);
    Page<Thought> getAllByUser(User user, Pageable pageable);
    Page<Thought> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable);
    Page<Thought> getAllPublished(Pageable pageable);
    Optional<Thought> getById(Long id);
    void delete(Long id);
    Thought updateStatus(Long id, PublicationStatus status);
    void incrementViewCount(Long id);
}
