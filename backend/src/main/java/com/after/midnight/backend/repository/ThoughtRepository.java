package com.after.midnight.backend.repository;

import com.after.midnight.backend.model.Thought;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThoughtRepository extends JpaRepository<Thought, Long> {
}
