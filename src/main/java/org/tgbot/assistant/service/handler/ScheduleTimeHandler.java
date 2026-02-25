package org.tgbot.assistant.service.handler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.service.UserCacheService;
import org.tgbot.assistant.service.UserService;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

// Установка времени урока

@Component
@RequiredArgsConstructor
public class ScheduleTimeHandler implements InputMessageHandler{

    private final UserService userService;
    private final UserCacheService userCacheService;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        try{
            LocalTime time = LocalTime.parse(text);

            Schedule draft = userCacheService.getDraft(chatId);
            draft.setTime(time);
            userCacheService.saveDraft(chatId, draft);

            userService.updateBotState(chatId, BotState.WAITING_FOR_SCHEDULE_TITLE);
            return new SendMessage(chatId.toString(), "Время принято! \nОсталось последнее: как называется урок?");

        }
        catch (DateTimeParseException e){
            return new SendMessage(chatId.toString(), "Неверный формат времени! \nПожалуйста, введите ЧЧ:ММ (например 09:00 или 18:45)");
        }

    }
    // Хендлер слушает сообщения только тогда, когда пользователь находится в состоянии WAITING_FOR_SCHEDULE_TIME
    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_SCHEDULE_TIME;
    }
}
