package com.example.library.management.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AccessEnforcer
{
    private static final Logger log = LoggerFactory.getLogger(AccessEnforcer.class);

    public void requireAdmin() {
        AuthenticatedUser currentUser = getCurrentUser();

        if (currentUser.role() != UserRole.ADMIN) {
            log.warn("Admin access denied: userId={}, email={}, role={}",
                    currentUser.id(),
                    currentUser.email(),
                    currentUser.role()
            );

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required.");
        }
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Access denied because request is unauthenticated");

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required.");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof AuthenticatedUser authenticatedUser)) {
            log.warn("Access denied because principal has unexpected type: {}",
                    principal == null ? null : principal.getClass().getName()
            );

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required.");
        }

        return authenticatedUser;
    }
}
