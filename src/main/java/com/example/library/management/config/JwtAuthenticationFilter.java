package com.example.library.management.config;

import com.example.library.management.security.AuthenticatedUser;
import com.example.library.management.security.JwtService;
import com.example.library.management.security.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            Claims claims = jwtService.parseClaims(token);

            String email = claims.getSubject();
            String role = claims.get("role", String.class);
            Long id = claims.get("id", Long.class);

            var authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );

            var principal = new AuthenticatedUser(
                    id,
                    email,
                    UserRole.valueOf(role)
            );

            var authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ExpiredJwtException exception) {
            log.warn(
                    "Expired JWT: method={}, uri={}, subject={}, expiredAt={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    exception.getClaims().getSubject(),
                    exception.getClaims().getExpiration()
            );
            SecurityContextHolder.clearContext();
        } catch (SignatureException exception) {
            log.warn(
                    "Invalid JWT signature: method={}, uri={}",
                    request.getMethod(),
                    request.getRequestURI()
            );
            SecurityContextHolder.clearContext();
        } catch (MalformedJwtException exception) {
            log.warn(
                    "Malformed JWT: method={}, uri={}",
                    request.getMethod(),
                    request.getRequestURI()
            );
            SecurityContextHolder.clearContext();
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn(
                    "Invalid JWT: method={}, uri={}, reason={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    exception.getMessage()
            );

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
