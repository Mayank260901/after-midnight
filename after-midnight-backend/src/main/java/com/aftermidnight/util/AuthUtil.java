package com.aftermidnight.util;

import com.aftermidnight.entity.User;
import com.aftermidnight.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for authentication related operations.
 */
@Component
public class AuthUtil {

    /**
     * Gets the current logged-in user entity.
     * @return The current User entity, or null if not authenticated.
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUser();
        }
        return null;
    }

    /**
     * Gets the current logged-in user's ID.
     * @return The user ID, or null if not authenticated.
     */
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return (user != null) ? user.getId() : null;
    }

    /**
     * Gets the current logged-in user's email.
     * @return The user email, or null if not authenticated.
     */
    public String getCurrentUserEmail() {
        User user = getCurrentUser();
        return (user != null) ? user.getEmail() : null;
    }
}
