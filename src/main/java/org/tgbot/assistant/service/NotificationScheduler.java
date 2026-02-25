package org.tgbot.assistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tgbot.assistant.bot.SmartBot;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.repository.ScheduleRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Этот сервис позволяет автоматизировать бизнес-процессы, пробуждает бота
 */

// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
// Аннотация для логгера
@Slf4j
//Генерирует конструктор для всех final-полей
@RequiredArgsConstructor
public class NotificationScheduler {
    // Нужен для фактической отправки сообщений
    private final SmartBot smartBot;
    // Нужен для поиска данных в БД
    private final ScheduleRepository scheduleRepository;

    // Аннотация, которая говорит, выполнять задачу каждую минуту, когда секунды равны 00
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendNotifications(){
        // Получаем текущее время и округляем его до минут
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        log.info("Запуск шедулера: ищем занятия на {} в {}", today, currentTime);

        // Делаем запрос в БД
        List<Schedule> activeSchedules = scheduleRepository.findByDayOfWeekAndTimeAndIsActiveTrue(today, currentTime);

        // Если что-то нашли, то проходим циклом по списку
        for (Schedule schedule : activeSchedules) {
            // Достаем Telegram ID пользователя, которому принадлежит это расписание
            Long chatId = schedule.getUser().getTgId();
            String messageText = "Напоминание! \nСейчас начнется занятие: " + schedule.getTitle();

            // Отправляем уведомление
            sendToUser(chatId, messageText);
        }
    }

    // Метод для упаковки текста в объект SendMessage
    private void sendToUser(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            // Обращаемся к боту напрямую для отправки
            smartBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить напоминание пользователю {}: {}", chatId, e.getMessage());
        }
    }
}
