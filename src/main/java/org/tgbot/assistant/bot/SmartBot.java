package org.tgbot.assistant.bot;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.service.UserService;

@Slf4j
@Component
public class SmartBot extends TelegramLongPollingBot {

    @Value("${bot.name")
    private String botName;

    private final UserService userService;

    public SmartBot(@Value("${bot.token}") String botToken, UserService userService) {
        super(botToken);
        this.userService = userService;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();

            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            log.info("Получено сообщение: '{}' от пользователя: {}", messageText, username);

            User user = userService.getOrCreateUser(chatId, username);
            if ("/start".equals(messageText)) {
                sendMessage(chatId, "Привет, " + user.getUsername() + "! Я твой умный ассистент. \n" +
                        "Мой State в базе данных: " + user.getBotState());
            } else {
                sendMessage(chatId, "Ты сказал: " + messageText);
            }
        }

    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage());
        }
    }


}
