package org.tgbot.assistant.entity;

public enum BotState {
    IDLE,                  // Обычное состояние (ничего не ждем)
    WAITING_FOR_SCHEDULE_DAY,   // Ждем ввода дня недели
    WAITING_FOR_SCHEDULE_TIME,  // Ждем ввода времени
    WAITING_FOR_SCHEDULE_TITLE,  // Ждем названия предмета
    WAITING_FOR_AI_QUESTION,    // Ждем вопрос для ИИ
    WAITING_FOR_DOTA_ID // Ждем запрос ранга
}
