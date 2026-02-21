package org.tgbot.assistant.entity;

public enum BotState {
    IDLE,                  // Обычное состояние (ничего не ждем)
    WAITING_FOR_SCHEDULE_DAY, // Ждем когда юзер введет день недели
    WAITING_FOR_SCHEDULE_TIME, // Ждем время занятия
    WAITING_FOR_DOTA_ID    // Ждем ID профиля доты
}
