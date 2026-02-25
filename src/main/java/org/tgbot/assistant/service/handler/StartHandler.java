package org.tgbot.assistant.service.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.tgbot.assistant.entity.BotState;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartHandler implements InputMessageHandler {

    @Override
    public SendMessage handle(Message message) {
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId().toString());
        response.setText("Привет! Я твой умный ассистент. Выбери действие:");

        // Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true); // Кнопки будут компактными
        keyboardMarkup.setOneTimeKeyboard(false); // Клавиатура не исчезнет

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строка кнопок
        KeyboardRow row1 = new KeyboardRow();
        row1.add("/schedule");
        row1.add("/dota");

        // Вторая строка
        KeyboardRow row2 = new KeyboardRow();
        row2.add("/ai");
        row2.add("/list");

        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        response.setReplyMarkup(keyboardMarkup);

        return response;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.IDLE;
    }
}