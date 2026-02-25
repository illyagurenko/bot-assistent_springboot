package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.repository.ScheduleRepository;
import org.tgbot.assistant.repository.UserRepository;

import java.util.List;

// Возвращение всего расписания

@Component
@RequiredArgsConstructor
public class ScheduleListHandler implements InputMessageHandler {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        User user = userRepository.findByTgId(chatId).orElseThrow();

        List<Schedule> schedules = scheduleRepository.findAllByUserOrderByDayOfWeekAscTimeAsc(user);

        if (schedules.isEmpty()) {
            return new SendMessage(chatId.toString(), "Твое расписание пока пусто. Добавь что-нибудь через /schedule");
        }

        StringBuilder sb = new StringBuilder("Твое расписание:\n\n");
        for (Schedule s : schedules) {
            sb.append(String.format(" %s в %s — %s\n",
                    s.getDayOfWeek(),
                    s.getTime(),
                    s.getTitle()));
        }

        return new SendMessage(chatId.toString(), sb.toString());
    }

    @Override
    public BotState getHandlerName() {
        return null;
    }
}