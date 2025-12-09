package com.example.GymApplication.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final OwnerDetailsService ownerDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, OwnerDetailsService ownerDetailsService) {
        this.jwtUtils = jwtUtils;
        this.ownerDetailsService = ownerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token = null;
        if (header != null && header.startsWith("Bearer ")) token = header.substring(7);

        // debug logs (remove or use logger in prod)
        System.out.println("JwtFilter - header: " + header);

        if (token != null) {
            // token present from client -> must validate
            boolean valid = false;
            try {
                valid = jwtUtils.validateToken(token);
            } catch (Exception e) {
                valid = false;
            }

            if (!valid) {
                // token was provided but invalid/expired -> block immediately
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return; // DO NOT call filterChain.doFilter -> terminate request here
            }

            // valid token -> load user and set security context
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtUtils.getUsernameFromToken(token);
                UserDetails userDetails = ownerDetailsService.loadUserByUsername(username);

                if (userDetails == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                    return;
                }

                // Optional: check user active/locked flags from userDetails or DB
                // if (!((CustomUserDetails) userDetails).isActive()) { ... sendError ...; return; }

                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // no token provided -> let the security chain decide (some endpoints may be public)
        filterChain.doFilter(request, response);
    }

}
