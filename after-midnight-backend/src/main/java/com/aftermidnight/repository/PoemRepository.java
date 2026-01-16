package com.aftermidnight.repository;

import com.aftermidnight.entity.Poem;
import com.aftermidnight.entity.PublicationStatus;
import com.aftermidnight.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoemRepository extends JpaRepository<Poem, Long> {
    @EntityGraph(attributePaths = "user")
    Page<Poem> findByUserAndDeletedFalse(User user, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Poem> findByUserAndStatusAndDeletedFalse(User user, PublicationStatus status, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    java.util.Optional<Poem> findByIdAndDeletedFalse(Long id);

    @EntityGraph(attributePaths = "user")
    Page<Poem> findByStatusAndDeletedFalse(PublicationStatus status, Pageable pageable);
}
