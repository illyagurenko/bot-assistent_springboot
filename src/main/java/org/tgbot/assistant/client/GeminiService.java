package org.tgbot.assistant.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tgbot.assistant.dto.GeminiRequest;
import org.tgbot.assistant.dto.GeminiResponse;

import java.util.List;

@Service
public class GeminiService {

    @Value("${api.gemini.key}")
    private String apiKey;

    private final RestClient restClient;

    public GeminiService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public String askGemini(String prompt) {
        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );


        String url = "/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        try {
            GeminiResponse response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
        } catch (Exception e) {

            return "Ошибка при вызове Gemini 2.0: " + e.getMessage();
        }
        return "ИИ прислал пустой ответ.";
    }
}