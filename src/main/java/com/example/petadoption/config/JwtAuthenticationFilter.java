package com.example.petadoption.config;

import com.example.petadoption.auth.JwtUtil;
import com.example.petadoption.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    	String path = request.getRequestURI();

    	// Normalize: remove context path if present
    	String basePath = request.getContextPath();
    	if (basePath != null && !basePath.isEmpty() && path.startsWith(basePath)) {
    	    path = path.substring(basePath.length());
    	}

    	// Skip all /api/auth/* endpoints (login, signup, register)
    	if (path.startsWith("/api/auth/")) {
    	    filterChain.doFilter(request, response);
    	    return;
    	}

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        // 🔹 Extract token from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.getEmailFromToken(token);
            } catch (Exception e) {
                // Invalid token → just continue without authentication
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        // 🔹 If we got email and no authentication yet, validate and set context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
