package org.tgbot.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

// объект для передачи данных

@Data
@AllArgsConstructor
public class ChatRequest {
    private String model;
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    // Вложенный статический класс Message. Он описывает структуру одного сообщения в списке
    public static class Message {
        private String role;    // "user" или "system"
        private String content; // Текст вопроса
    }
}