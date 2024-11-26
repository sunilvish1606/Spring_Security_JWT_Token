package com.sstoken.utility;

import com.sstoken.entity.UserData;
import com.sstoken.repository.UserDataRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private UserDataRepository userDataRepository;

    public JwtFilter(JwtUtils jwtUtils, UserDataRepository userDataRepository) {
        this.jwtUtils = jwtUtils;
        this.userDataRepository = userDataRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract JWT token from request header and validate it
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null && token.startsWith("Bearer ")) {
            // Validate JWT token and extract username
            try {
                String jwtToken = token.substring(8, token.length() - 1);
                String username = jwtUtils.getUsername(jwtToken);
                // Add authenticated username to request context for further processing
                Optional<UserData> opUserData = userDataRepository.findByUsername(username);
                if(opUserData.isPresent()){
                    UserData userData = opUserData.get();
                    // Set authenticated user details in Spring Security context
                    UsernamePasswordAuthenticationToken auth = new
                            UsernamePasswordAuthenticationToken(userData, null, Collections.singleton(new SimpleGrantedAuthority(userData.getRole())));
                    auth.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                System.err.println("JWT validation failed: " + e.getMessage());
            }
        }
        // Pass control to the next filter in the chain if authentication is successful
        filterChain.doFilter(request, response);
    }
}
