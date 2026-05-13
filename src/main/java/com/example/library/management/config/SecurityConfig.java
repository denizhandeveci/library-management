package com.example.library.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admins/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/create-user").permitAll()

                        // Temporarily public for bootstrapping.
                        // Later, make this ADMIN-only or replace it with a seed script.
                        // TODO, yb
                        .requestMatchers(HttpMethod.POST, "/admins").permitAll()

                        // Book reads: any authenticated user/admin
                        .requestMatchers(HttpMethod.GET, "/books").authenticated()
                        .requestMatchers(HttpMethod.GET, "/books/**").authenticated()

                        // Book writes: admin only
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/reset").hasRole("ADMIN")

                        // User management: admin only
                        .requestMatchers(HttpMethod.GET, "/get-all-users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/delete-user/**").hasRole("ADMIN")

                        // Everything else requires login
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
