package com.aftermidnight.repository;

import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.Thought;
import com.aftermidnight.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThoughtRepository extends JpaRepository<Thought, Long> {
    @EntityGraph(attributePaths = "user")
    Page<Thought> findByUserAndDeletedFalse(User user, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Thought> findByUserAndStatusAndDeletedFalse(User user, PublicationStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    java.util.Optional<Thought> findByIdAndDeletedFalse(Long id);

    @EntityGraph(attributePaths = "user")
    Page<Thought> findByStatusAndDeletedFalse(PublicationStatus status, Pageable pageable);
}
