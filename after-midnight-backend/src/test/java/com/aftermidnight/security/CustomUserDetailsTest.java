package com.aftermidnight.security;

import com.aftermidnight.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private User user;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void getUsernameReturnsEmail() {
        assertEquals("test@example.com", customUserDetails.getUsername());
    }

    @Test
    void getPasswordReturnsPassword() {
        assertEquals("encodedPassword", customUserDetails.getPassword());
    }

    @Test
    void getAuthoritiesReturnsDefaultRole() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void accountStatusMethodsReturnTrue() {
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());
    }

    @Test
    void getUserReturnsWrappedUser() {
        assertEquals(user, customUserDetails.getUser());
    }
}
