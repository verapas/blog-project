package com.example.blog.resources.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Lazy
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Stelle sicher, dass dieser Filter vor anderen hinzugefügt wird
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/users/login", "/users/register").permitAll() // Erlaube öffentliche Zugriffe auf diese Endpunkte
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Öffentliche Dokumentationsendpunkte
                        .requestMatchers(HttpMethod.POST, "/posts").hasAnyRole("USER", "ADMIN") // Berechtigung für das Erstellen von Posts
                        .anyRequest().authenticated() // Alle anderen Anfragen müssen authentifiziert sein
                );

        return http.build();
    }

    /**
     * Definiert einen Bean für die Passwortverschlüsselung mit BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
