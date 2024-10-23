package com.example.blog.resources.security;

import com.example.blog.resources.entity.User;
import com.example.blog.resources.service.UserService;
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

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    /**
     * Diese Methode wird für jede eingehende HTTP-Anfrage aufgerufen.
     * Sie extrahiert das JWT aus der Autorisierungs-Header, validiert es und setzt die Authentifizierung im SecurityContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Prüfe, ob das Authorization-Header vorhanden ist und starte mit "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);  // Entferne 'Bearer ' und hole den JWT-Token
            username = Jwts.parser()
                    .setSigningKey(tokenService.getSecretKey())
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();  // Hole den Benutzernamen aus dem Token
        }

        // Wenn der Benutzername vorhanden ist und der Benutzer nicht authentifiziert ist
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findUserEntityByUsername(username);  // Benutzer anhand des Benutzernamens finden
            if (user != null) {
                MyUserPrincipal userPrincipal = new MyUserPrincipal(user);

                // Erstelle ein AuthenticationToken für den Benutzer
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

                // Setze die Authentifizierung im SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Setze die Filterkette fort
        chain.doFilter(request, response);
    }

    /**
     * Extrahiert den Benutzernamen (oder die E-Mail) aus einem gegebenen JWT.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(tokenService.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Benutzernamen aus dem Token extrahieren
    }
}
