package com.after.midnight.backend.repository;

import com.after.midnight.backend.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByTokenAndActiveTrue(String token);
}

