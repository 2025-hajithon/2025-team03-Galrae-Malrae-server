package com.backend.domain;

import com.backend.global.gemini.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
@RequiredArgsConstructor
public class GeminiTestController {

    private final GeminiService geminiService;

    @PostMapping("/test")
    public String test(@RequestBody String prompt) {
        return geminiService.getContentofPrompt(prompt);
    }
}
