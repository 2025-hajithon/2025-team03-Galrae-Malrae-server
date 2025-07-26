package com.backend.global.config;

import com.backend.global.properties.GeminiProperties;
import com.backend.global.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtProperties.class, GeminiProperties.class})
@Configuration
public class PropertiesConfig {}
