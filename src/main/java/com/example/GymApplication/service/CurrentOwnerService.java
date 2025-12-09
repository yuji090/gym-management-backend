package com.example.GymApplication.service;


import com.example.GymApplication.model.Owner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CurrentOwnerService {

    public Long getCurrentOwnerId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof com.example.GymApplication.security.AppOwnerDetails)) {
            throw new AccessDeniedException("Unauthenticated - token required");
        }
        return ((com.example.GymApplication.security.AppOwnerDetails) auth.getPrincipal()).getId();
    }

    public Owner getCurrentOwner() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof com.example.GymApplication.security.AppOwnerDetails)) {
            throw new AccessDeniedException("Unauthenticated - token required");
        }
        return ((com.example.GymApplication.security.AppOwnerDetails) auth.getPrincipal()).getOwner();
    }
}
