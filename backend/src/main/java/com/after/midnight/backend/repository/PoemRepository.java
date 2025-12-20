package com.after.midnight.backend.repository;

import com.after.midnight.backend.model.Poem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoemRepository extends JpaRepository<Poem, Long> {
}

