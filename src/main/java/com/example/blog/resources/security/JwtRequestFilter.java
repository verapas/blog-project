package com.example.blog.resources.security;

import com.example.blog.resources.entity.User;
import com.example.blog.resources.service.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
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
            chain.doFilter(request, response); // Filter für diese Endpunkte überspringen
            return;
        }

        // JWT-Verarbeitung für geschützte Endpunkte
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = Jwts.parser()
                        .setSigningKey(tokenService.getSecretKey())
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (JwtException e) {
                // Wenn das JWT ungültig oder abgelaufen ist, gib eine 403 Forbidden Antwort zurück
                logger.error("Ungültiger JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } else {
            // Logge einen Hinweis, dass der Authorization Header fehlt oder fehlerhaft ist
            logger.warn("Authorization Header ist entweder null oder hat kein Bearer-Präfix.");
        }

        // Setze den SecurityContext, falls Benutzername vorhanden ist und der Benutzer nicht schon authentifiziert ist
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findUserEntityByUsername(username);
            if (user != null) {
                MyUserPrincipal userPrincipal = new MyUserPrincipal(user);
                logger.info("Benutzerrolle: " + user.getRole());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                logger.info("Benutzer " + username + " wurde erfolgreich authentifiziert mit Rollen: " + userPrincipal.getAuthorities());
            } else {
                logger.warn("Benutzer mit dem Benutzernamen konnte nicht gefunden werden.");
            }
        }

        // Führe den Filter weiter aus, nachdem die Authentifizierung abgeschlossen ist
        chain.doFilter(request, response);
    }
}

