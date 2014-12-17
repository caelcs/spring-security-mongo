package uk.co.caeldev.springsecuritymongo.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextService {
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void setAuthentication(final Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
