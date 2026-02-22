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

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationScheduler {
    private final SmartBot smartBot;
    private final ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendNotifications(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        log.info("Запуск шедулера: ищем занятия на {} в {}", today, currentTime);

        List<Schedule> activeSchedules = scheduleRepository.findByDayOfWeekAndTimeAndIsActiveTrue(today, currentTime);

        for (Schedule schedule : activeSchedules) {
            Long chatId = schedule.getUser().getTgId();
            String messageText = "Напоминание! \nСейчас начнется занятие: " + schedule.getTitle();

            sendToUser(chatId, messageText);
        }
    }
    private void sendToUser(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            smartBot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Не удалось отправить напоминание пользователю {}: {}", chatId, e.getMessage());
        }
    }
}
