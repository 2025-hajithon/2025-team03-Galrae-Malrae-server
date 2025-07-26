package com.backend.global.config;

import com.backend.global.security.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtProperties.class})
@Configuration
public class PropertiesConfig {}
