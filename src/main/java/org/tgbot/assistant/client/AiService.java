package org.tgbot.assistant.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tgbot.assistant.dto.ChatRequest;
import org.tgbot.assistant.dto.ChatResponse;

import java.util.List;

/**
 * Сервис для запросов к API ИИ
 */

// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
public class AiService {

    // Внедряем API ключ и URL из настроек
    @Value("${api.ai.key}")
    private String apiKey;

    @Value("${api.ai.url}")
    private String apiUrl;

    // RestClient - позволяет отправлять HTTP-запросы
    private final RestClient restClient;

    public AiService() {
        // Создаем экземпляр клиента
        this.restClient = RestClient.builder().build();
    }

    // Основной метод для отправки вопроса нейросети
    public String askAi(String prompt) {
        // Формируем запрос, cоздаем DTO
        ChatRequest request = new ChatRequest(
                "stepfun/step-3.5-flash:free", // Бесплатная и мощная модель
                List.of(new ChatRequest.Message("user", prompt))
        );

        try {
            // POST-запрос на сервер OpenRouter
            ChatResponse response = restClient.post()
                    .uri(apiUrl) // Куда отправляем
                    .header("Authorization", "Bearer " + apiKey) // Авторизация
                    // Заголовки для аналитики OpenRouter
                    .header("HTTP-Referer", "https://github.com/my-tg-bot")
                    .header("X-Title", "Java Telegram Bot")
                    .contentType(MediaType.APPLICATION_JSON) // Говорим, что отправляем JSON
                    .body(request) // Кладем наше тело запроса
                    .retrieve() // Выполняем запрос
                    .body(ChatResponse.class); // Просим Spring превратить полученный JSON-ответ в Java-объект ChatResponse

            // ИИ возвращает список вариантов. Мы берем первый вариант и достаем из него текст сообщения
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            return "Ошибка AI (OpenRouter): " + e.getMessage();
        }
        return "ИИ не смог ответить.";
    }
}