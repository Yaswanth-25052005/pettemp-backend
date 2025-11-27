package com.example.petadoption.config;

import com.example.petadoption.auth.JwtUtil;
import com.example.petadoption.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 *
 * Responsibilities:
 *  - Skip public endpoints (e.g. /api/auth/**)
 *  - Allow OPTIONS preflight through
 *  - Parse Bearer token if present and set Authentication if valid
 *  - Never throw: on token errors just log and continue the filter chain
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Fast-path: allow preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Normalize path: remove context path if present
        String path = request.getRequestURI();
        String basePath = request.getContextPath();
        if (basePath != null && !basePath.isEmpty() && path.startsWith(basePath)) {
            path = path.substring(basePath.length());
        }

        // Skip authentication for auth endpoints and static/uploads if desired
        if (path.startsWith("/api/auth/") || path.startsWith("/uploads/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7).trim();
                if (!token.isEmpty()) {
                    // Extract email (or username) in a guarded way
                    try {
                        email = jwtUtil.getEmailFromToken(token);
                    } catch (Exception ex) {
                        log.debug("Failed to extract email from JWT: {}", ex.getMessage());
                        // Do not short-circuit; just treat as unauthenticated
                    }
                }
            }
        } catch (Exception ex) {
            // Defensive: any unexpected exception reading header shouldn't stop processing
            log.warn("Unexpected error while reading Authorization header: {}", ex.getMessage(), ex);
        }

        // If we obtained an email and there is no authentication yet, validate and set it
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                // Validate token against user details. If your JwtUtil has a different signature,
                // adapt this call accordingly (e.g. validateToken(token, userDetails))
                boolean valid = false;
                try {
                    valid = jwtUtil.validateToken(token);
                } catch (Exception ex) {
                    log.debug("JWT validation failed for token: {}", ex.getMessage());
                }

                if (valid) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("JWT authenticated user: {}", email);
                } else {
                    log.debug("JWT invalid or expired for user: {}", email);
                }

            } catch (Exception ex) {
                // Could be UsernameNotFoundException or DB connection issue - log and continue
                log.debug("Unable to load user details for '{}': {}", email, ex.getMessage());
            }
        }

        // Always continue the filter chain (important for permitAll endpoints)
        filterChain.doFilter(request, response);
    }
}
