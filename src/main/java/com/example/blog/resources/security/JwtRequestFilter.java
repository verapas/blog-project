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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Hole die URI der Anfrage
        String path = request.getRequestURI();

        // Überspringe den JWT-Filter für offene Endpunkte wie /users/register oder /login
        if ("/users/register".equals(path) || "/login".equals(path)) {
            chain.doFilter(request, response); // Überspringe den Filter
            return;
        }

        // JWT-Verarbeitung für geschützte Endpunkte
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = Jwts.parser()
                    .setSigningKey(tokenService.getSecretKey())
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findUserEntityByUsername(username);
            if (user != null) {
                MyUserPrincipal userPrincipal = new MyUserPrincipal(user);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Führe den Filter weiter aus, nachdem die Authentifizierung abgeschlossen ist
        chain.doFilter(request, response);
    }
}
