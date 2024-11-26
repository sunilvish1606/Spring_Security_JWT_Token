package com.sstoken.configuration;

import com.sstoken.utility.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        // Disable CSRF and CORS for this simple example. In a production environment, you should enable these features.
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        // Apply JWT filter before authorization filter to validate JWT token and authorize requests.
        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);

        // Allow all requests to pass through. In a production environment, you should restrict this to only authorized users.
        // http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // Example of role-based access control (RBAC). Only admin, user, and owner roles can access these APIs.
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/test/admin/test").hasRole("ADMIN")
                .requestMatchers("/api/v1/test/user/test").hasRole("USER")
                .requestMatchers("/api/v1/test/owner/test").hasRole("OWNER")
                .requestMatchers("/api/v1/user/signup/**", "/api/v1/user/login", "/api/v1/test/home/test").permitAll()
                .anyRequest().authenticated()
        );
        return http.build();
    }
}
