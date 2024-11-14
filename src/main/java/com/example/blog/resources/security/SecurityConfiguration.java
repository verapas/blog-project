package com.example.blog.resources.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests()
                .requestMatchers("/login", "/users/register").permitAll()  // Erlaube öffentliche Zugriffe auf diese Endpunkte
                .anyRequest().authenticated()  // Alle anderen Anfragen müssen authentifiziert werden


                ;
        return http.build();
    }
}
