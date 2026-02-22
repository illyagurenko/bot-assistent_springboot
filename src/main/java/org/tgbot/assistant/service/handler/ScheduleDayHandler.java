package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import java.time.DayOfWeek;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.service.UserCacheService;
import org.tgbot.assistant.service.UserService;



@Component
@RequiredArgsConstructor
public class ScheduleDayHandler implements InputMessageHandler{

    private final UserService userService;
    private final UserCacheService userCacheService;

    @Override
    public SendMessage handle(Message message) {
        String text = message.getText().toUpperCase();
        Long chatId = message.getChatId();

        try{
            DayOfWeek day = DayOfWeek.valueOf(text);

            Schedule draft = userCacheService.getDraft(chatId);
            draft.setDayOfWeek(day);
            userCacheService.saveDraft(chatId, draft);
            userService.updateBotState(chatId, BotState.WAITING_FOR_SCHEDULE_TIME);

            return new SendMessage(chatId.toString(), " День записан: " + day + "\nТеперь введите время урока (формат HH:mm):");

        }
        catch (IllegalArgumentException e){
            return new SendMessage(chatId.toString(), "Непонятный день недели! \nПожалуйста, введите на английском: MONDAY, TUESDAY, WEDNESDAY...");
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_SCHEDULE_DAY;
    }
}
