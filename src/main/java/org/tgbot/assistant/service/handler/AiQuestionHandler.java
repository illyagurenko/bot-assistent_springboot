package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.client.GeminiService;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.service.UserService;

@Component
@RequiredArgsConstructor
public class AiQuestionHandler implements InputMessageHandler {

    private final GeminiService geminiService;
    private final UserService userService;

    @Override
    public SendMessage handle(Message message) {
        String question = message.getText();
        Long chatId = message.getChatId();

        String answer = geminiService.askGemini(question);


        return new SendMessage(chatId.toString(), answer);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_AI_QUESTION;
    }
}