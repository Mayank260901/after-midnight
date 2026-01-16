package com.aftermidnight.service;

import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PoemService {
    Poem create(Poem poem);
    Page<Poem> getAllByUser(User user, Pageable pageable);
    Page<Poem> getAllByUserAndStatus(User user, PublicationStatus status, Pageable pageable);
    Page<Poem> getAllPublished(Pageable pageable);
    Optional<Poem> getById(Long id);
    void delete(Long id);
    Poem updateStatus(Long id, PublicationStatus status);
    void incrementViewCount(Long id);
}
