package com.backend.global.gemini.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GeminiRequest {

    private final List<Content> contents;
    private final GenerationConfig generationConfig;

    public GeminiRequest(String prompt) {
        this.contents = List.of(new Content(new Parts(prompt)));
        this.generationConfig = new GenerationConfig(1, 60, 0.7);
    }

    @Data
    @AllArgsConstructor
    private static class Content {
        private Parts parts;
    }

    @Data
    @AllArgsConstructor
    private static class Parts {
        private String text;
    }

    @Data
    @AllArgsConstructor
    private static class GenerationConfig {
        @JsonProperty("candidate_count")
        private int candidateCount; // 응답 수
        @JsonProperty("max_output_tokens")
        private int maxOutputTokens; // 출력 단어 수 제한
        private double temperature; // 1에 가까울수록 창의적
    }
}
