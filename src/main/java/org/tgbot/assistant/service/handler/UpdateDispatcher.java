package org.tgbot.assistant.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.service.UserService;
import org.tgbot.assistant.service.handler.InputMessageHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UpdateDispatcher {
    private final Map<BotState, InputMessageHandler> handlers;
    private final UserService userService;
    private final ScheduleStartHandler scheduleStartHandler;

    @Autowired
    public UpdateDispatcher(List<InputMessageHandler> messageHandlers, UserService userService,ScheduleStartHandler scheduleStartHandler) {
        this.userService = userService;
        this.scheduleStartHandler = scheduleStartHandler;
        this.handlers = messageHandlers.stream()
                .filter(h -> h.getHandlerName() != null)
                .collect(Collectors.toMap(InputMessageHandler::getHandlerName, Function.identity()));
    }

    public SendMessage distribute(Update update){
        if(!update.hasMessage() || !update.getMessage().hasText()){
            return null;
        }

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        User user = userService.getOrCreateUser(chatId, message.getFrom().getUserName());

        if ("/start".equals(text)){
            userService.updateBotState(chatId, BotState.IDLE);
            return handlers.get(BotState.IDLE).handle(message);
        }

        if("/schedule".equals(text)){
            return scheduleStartHandler.handle(message);
        }

        BotState currentState = user.getBotState();
        InputMessageHandler handler = handlers.get(currentState);

        if(handler == null){
            log.warn("Handler for state {} not found", currentState );
            return new SendMessage(chatId.toString(), "неизвестная команда или состояние");
        }
        return handler.handle(message);
    }
}
