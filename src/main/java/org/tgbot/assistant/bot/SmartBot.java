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
import org.tgbot.assistant.service.handler.UpdateDispatcher;

@Slf4j
@Component
public class SmartBot extends TelegramLongPollingBot {

    @Value("${bot.name")
    private String botName;

    private final UpdateDispatcher updateDispatcher;

    public SmartBot(@Value("${bot.token}") String botToken, UpdateDispatcher updateDispatcher) {
        super(botToken);
        this.updateDispatcher = updateDispatcher;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = updateDispatcher.distribute(update);

        if(sendMessage != null){
            try{
                execute(sendMessage);
            }
            catch (TelegramApiException e){
                log.error("Error sending message: {}", e.getMessage());
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
