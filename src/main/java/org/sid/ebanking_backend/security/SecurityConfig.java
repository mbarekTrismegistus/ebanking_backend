package org.sid.ebanking_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for the Digital Banking REST API.
 *
 * What is configured here (as shown in the course):
 *
 *  1. CSRF disabled – REST APIs that use token-based auth do not need CSRF protection.
 *
 *  2. CORS enabled – Angular (running on e.g. localhost:4200) must be able to call
 *     the Spring Boot backend (running on e.g. localhost:8085).
 *     Without CORS configuration the browser will block cross-origin requests.
 *
 *  3. All endpoints are permitted without authentication for now.
 *     In Part 3 the instructor will add JWT-based authentication on top of this.
 *
 *  4. Stateless session – REST APIs should not maintain server-side HTTP sessions.
 *     This is the "stateless authentication" mode mentioned in the course.
 *
 *  5. Swagger UI and H2 console are explicitly permitted so developers can test
 *     the API without logging in.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF – not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Configure CORS – required so the Angular front-end can call the API
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless session – no server-side HTTP session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Authorisation rules
            .authorizeHttpRequests(auth -> auth
                // Allow Swagger UI
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // Allow H2 console (dev only)
                .requestMatchers("/h2-console/**").permitAll()
                // For now allow everything – JWT filter will be added in Part 3
                .anyRequest().permitAll()
            )

            // Allow H2 console to be rendered in an iframe
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    /**
     * CORS configuration that allows the Angular development server
     * (or any origin during development) to call the REST API.
     *
     * In production, replace "*" with the actual Angular deployment URL.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
