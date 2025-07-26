package com.backend.global.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secretKey, Long accessTokenExpSeconds, Long refreshTokenExpSeconds) {}
