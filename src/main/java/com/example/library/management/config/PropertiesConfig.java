package com.example.library.management.config;

import com.example.library.management.security.JwtCookieProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtCookieProperties.class)
public class PropertiesConfig
{}
