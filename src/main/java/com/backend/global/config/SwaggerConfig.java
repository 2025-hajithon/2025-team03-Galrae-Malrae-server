package com.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.server}")
    private String serverUri;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(new Server().url("http://galraemalrae.duckdns.org").description("API 주소")));
    }

    private Info apiInfo() {
        return new Info()
                .title("갈래말래 API") // API의 제목
                .description("하지톤 2025 3팀 갈래말래 API 입니다.") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}
