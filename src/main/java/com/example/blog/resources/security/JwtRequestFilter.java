package com.example.blog.resources.security;

import com.example.blog.resources.entity.User;
import com.example.blog.resources.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Hole die URI der Anfrage
        String path = request.getRequestURI();

        // Definiere offene Endpunkte, die ohne Authentifizierung zugänglich sind
        List<String> openPaths = Arrays.asList("/users/register", "/users/login", "/swagger-ui", "/v3/api-docs");
        if (openPaths.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        // JWT-Verarbeitung für geschützte Endpunkte
        final String authorizationHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                // Hier wird angenommen, dass im JWT der Subject-Feld die E-Mail gespeichert wird
                email = Jwts.parser()
                        .setSigningKey(tokenService.getSecretKey())
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (JwtException e) {
                logger.error("Ungültiger JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
            logger.warn("Authorization Header ist entweder null oder hat kein Bearer-Präfix.");
        }

// Setze den SecurityContext, falls E-Mail im Token ist und noch keine Authentifizierung gesetzt wurde
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(tokenService.getSecretKey())
                        .parseClaimsJws(jwt)
                        .getBody();

                String role = claims.get("roles", String.class); // z. B. "ROLE_USER"
                if (role != null) {
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    logger.info("JWT-Authentifizierung erfolgreich: " + email + " mit Rolle: " + role);
                } else {
                    logger.warn("Keine Rolle im Token gefunden.");
                }

            } catch (Exception e) {
                logger.error("Fehler beim Verarbeiten des Tokens: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        // Führe den Filter weiter aus, nachdem die Authentifizierung abgeschlossen ist
        chain.doFilter(request, response);
    }
}

