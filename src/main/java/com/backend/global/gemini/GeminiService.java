package com.backend.global.gemini;

import com.backend.global.gemini.dto.request.GeminiRequest;
import com.backend.global.gemini.dto.response.GeminiResponse;
import com.backend.global.properties.GeminiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiProperties geminiProperties;

    public String getContentofPrompt(String prompt) {
        String requestUrl = geminiProperties.apiUrl() + "?key=" + geminiProperties.apiKey();
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<GeminiRequest> requestEntity = new HttpEntity<>(new GeminiRequest(prompt));
        ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                requestEntity,
                GeminiResponse.class
        );

        return response.getBody().getResponseText();
    }
}
