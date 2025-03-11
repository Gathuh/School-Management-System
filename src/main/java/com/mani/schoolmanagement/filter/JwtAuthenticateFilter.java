package com.mani.schoolmanagement.filter;

import com.mani.schoolmanagement.service.JwtService;
import com.mani.schoolmanagement.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import java.io.IOException;
import com.mani.schoolmanagement.model.User; // Change from org.springframework.security.core.userdetails.User
@Component
@RequiredArgsConstructor
public class JwtAuthenticateFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            // Allow /register and /login without token
            if (request.getRequestURI().equals("/register") || request.getRequestURI().equals("/login")) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            User user = new User(); // Add this line
            user.setUsername(userDetails.getUsername()); // Add this line
            if (jwtService.isValid(token, user)) { // Now 'user' is defined
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}