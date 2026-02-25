package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.client.AiService;
import org.tgbot.assistant.service.UserService;

@Component
@RequiredArgsConstructor
public class AiQuestionHandler implements InputMessageHandler {

    private final AiService aiService;
    private final UserService userService;

    // Логика обработки сообщений для ИИ
    @Override
    public SendMessage handle(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();

        // Если юзер ввел команду /ai (он еще в состоянии IDLE)
        if ("/ai".equals(text)) {
            // Мы меняем состояние пользователя в базе на ожидание вопроса для ИИ
            userService.updateBotState(chatId, BotState.WAITING_FOR_AI_QUESTION);
            return new SendMessage(chatId.toString(), "Задай любой вопрос:");
        }

        // Если юзер уже в состоянии ожидания вопроса - отправляем текст в нейросеть
        String aiResponse = aiService.askAi(text);


        // Чтобы после одного вопроса режим ИИ выключался - расскомментируй строку ниже:
        // userService.updateBotState(chatId, BotState.IDLE);

        return new SendMessage(chatId.toString(), aiResponse);
    }

    // Хендлер слушает сообщения только тогда, когда пользователь находится в состоянии WAITING_FOR_AI_QUESTION
    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_AI_QUESTION;
    }
}