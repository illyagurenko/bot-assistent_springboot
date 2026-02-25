package org.tgbot.assistant.dto;

import lombok.Data;
import java.util.List;

// Класс для понятного вида ответа от ИИ

@Data
public class ChatResponse {
    private List<Choice> choices;


    // Вложенный класс Choice (Вариант ответа)
    @Data
    public static class Choice {
        private Message message;
    }

    // Вложенный класс Message - итоговый отвеи
    @Data
    public static class Message {
        private String content;
    }
}