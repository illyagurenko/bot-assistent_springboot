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

/**
 * Это точка входа и выхода. Он реализует механизм Long Polling
 * бот постоянно спрашивает сервера Telegram: Есть ли новые сообщения?)
 */

// Аннотация для логгера
@Slf4j
// Аннотация для превращения класса в бин для спринга
@Component
public class SmartBot extends TelegramLongPollingBot {

    // Берем значение из application.yaml
    @Value("${bot.name")
    private String botName;

    // Ссылка на диспетчер, решающий, что делать с сообщением
    private final UpdateDispatcher updateDispatcher;

    // Конструктор бота
    public SmartBot(@Value("${bot.token}") String botToken, UpdateDispatcher updateDispatcher) {
        super(botToken);
        this.updateDispatcher = updateDispatcher;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    // Главный метод, срабатывающий когда кто-то пишет боту. update - объект содержащий всю информацию о сообщении
    @Override
    public void onUpdateReceived(Update update) {
        //отдаем задачу диспетчеру и принимаем готовый объект SendMessage - текст ответа
        SendMessage sendMessage = updateDispatcher.distribute(update);

        // Если диспетчер решил, что нужно ответить
        if(sendMessage != null){
            try{
                // Отправляем ответ в бота
                execute(sendMessage);
            }
            catch (TelegramApiException e){
                // Иначе выводим ошибку
                log.error("Error sending message: {}", e.getMessage());
            }
        }


    }
    //Вспомогательный метод для ручной отправки сообщений
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
