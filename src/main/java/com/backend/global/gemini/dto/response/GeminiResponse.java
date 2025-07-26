package com.backend.global.gemini.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {

    private List<Candidate> candidates;

    @Data
    private static class Candidate {
        private Content content;
    }

    @Data
    private static class Content {
        private List<Part> parts;
    }

    @Data
    private static class Part {
        private String text;
    }

    public String getResponseText() {
        return this.candidates
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();
    }
}