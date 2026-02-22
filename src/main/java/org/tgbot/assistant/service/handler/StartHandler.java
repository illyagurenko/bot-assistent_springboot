package org.tgbot.assistant.service.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.tgbot.assistant.entity.BotState;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class StartHandler implements InputMessageHandler{

    @Override
    public SendMessage handle(Message message) {
        return new SendMessage(
                message.getChatId().toString(),
                "я твой бот-ассистент. \nДоступные команды:\n/schedule - Добавить расписание\n/dota - Рейтинг в доте\n/ai - Чат с ИИ"
        );
    }

    @Override
    public BotState getHandlerName() {
        return BotState.IDLE;
    }
}
