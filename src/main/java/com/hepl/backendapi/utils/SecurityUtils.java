package com.hepl.backendapi.utils;

import com.hepl.backendapi.dto.generic.UserDTO;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Authentication getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Not authenticated");
        }

        return authentication;
    }

    public static String getCurrentAccountId() {
        return getAuthenticatedUser().getName();
    }

    public static UserDTO getCurrentUserDetails() {
        return (UserDTO) getAuthenticatedUser().getDetails();
    }
}
