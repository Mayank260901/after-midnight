package com.aftermidnight.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateTokenProducesValidToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsernameReturnsCorrectUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateTokenReturnsTrueForValidToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertTrue(jwtUtil.validateToken(token, username));
    }

    @Test
    void validateTokenReturnsFalseForDifferentUsername() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.validateToken(token, "otheruser"));
    }

    @Test
    void isTokenExpiredReturnsFalseForNewToken() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void extractExpirationReturnsFutureDate() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        Date expiration = jwtUtil.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }
}
