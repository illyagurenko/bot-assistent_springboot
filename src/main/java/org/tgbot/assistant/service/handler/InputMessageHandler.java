package org.tgbot.assistant.service.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.tgbot.assistant.entity.BotState;

public interface InputMessageHandler {
    SendMessage handle(Message message);
    BotState getHandlerName();
}
