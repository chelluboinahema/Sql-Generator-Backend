package com.hema.sqlbot.service;


import com.hema.sqlbot.modal.Ai.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class OpenAIService {

    private final WebClient webClient;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String generateResponse(String prompt) {

        OpenAIRequest request = new OpenAIRequest(
                "llama-3.3-70b-versatile",
                List.of(
                        new Message(
                                "You are an expert SQL assistant. Convert user requests into SQL queries.",
                                "system"
                        ),
                        new Message(
                                prompt,
                                "user"
                        )
                )
        );
        OpenAIResponse response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAIResponse.class)
                .block();
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("Empty response from Groq");
        }
        return response.getChoices()
                .get(0)
                .getMessage()
                .getContent()
                .trim();
    }
}