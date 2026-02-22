package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.service.UserService;

@Component
@RequiredArgsConstructor
public class ScheduleStartHandler implements InputMessageHandler {

    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        userService.updateBotState(message.getChatId(), BotState.WAITING_FOR_SCHEDULE_DAY);
        return new SendMessage(
                message.getChatId().toString(),
                "Добавляем новый урок! \nВведите день недели: "
        );
    }

    @Override
    public BotState getHandlerName() {
        return null;
    }
}
