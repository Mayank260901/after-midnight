package com.aftermidnight.util;

import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUtilTest {

    private AuthUtil authUtil;
    private User testUser;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        authUtil = new AuthUtil();
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
        customUserDetails = new CustomUserDetails(testUser);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_ReturnsUser_WhenAuthenticated() {
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        User currentUser = authUtil.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(testUser.getId(), currentUser.getId());
        assertEquals(testUser.getEmail(), currentUser.getEmail());
    }

    @Test
    void getCurrentUser_ReturnsNull_WhenNotAuthenticated() {
        User currentUser = authUtil.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    void getCurrentUserId_ReturnsId_WhenAuthenticated() {
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Long currentUserId = authUtil.getCurrentUserId();

        assertEquals(testUser.getId(), currentUserId);
    }

    @Test
    void getCurrentUserId_ReturnsNull_WhenNotAuthenticated() {
        Long currentUserId = authUtil.getCurrentUserId();
        assertNull(currentUserId);
    }

    @Test
    void getCurrentUserEmail_ReturnsEmail_WhenAuthenticated() {
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String currentUserEmail = authUtil.getCurrentUserEmail();

        assertEquals(testUser.getEmail(), currentUserEmail);
    }

    @Test
    void getCurrentUserEmail_ReturnsNull_WhenNotAuthenticated() {
        String currentUserEmail = authUtil.getCurrentUserEmail();
        assertNull(currentUserEmail);
    }
}
