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


import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Класс реализует паттерн Strategy. Диспетчер динамически находит нужный инструмент
 * (хендлер) для решения конкретной задачи. Он связывает Telegram API (Update) с Базой Данных
 */

// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
// Аннотация для логгера
@Slf4j
public class UpdateDispatcher {
    // Карта, где ключ — состояние бота, а значение — хендлер, который его обрабатывает
    private final Map<BotState, InputMessageHandler> handlers;
    private final UserService userService;
    private final ScheduleStartHandler scheduleStartHandler;
    private final ScheduleListHandler scheduleListHandler;

    // Конструктор, который находит все классы, реализующие InputMessageHandler
    @Autowired
    public UpdateDispatcher(List<InputMessageHandler> messageHandlers, UserService userService,ScheduleStartHandler scheduleStartHandler, ScheduleListHandler scheduleListHandler) {
        this.userService = userService;
        this.scheduleStartHandler = scheduleStartHandler;
        // Превращение списка хендлеров в быструю карту для поиска
        this.handlers = messageHandlers.stream()
                // Оставляем только те хендлеры, которые привязаны к конкретному BotState (не null)
                .filter(h -> h.getHandlerName() != null)
                // Ключ в карте — имя состояния (BotState), значение — сам объект хендлера
                .collect(Collectors.toMap(InputMessageHandler::getHandlerName, Function.identity()));
        this.scheduleListHandler = scheduleListHandler;
    }

    //  Главный метод распределения сообщений
    public SendMessage distribute(Update update){
        // Eсли это не текстовое сообщение - игнорируем
        if(!update.hasMessage() || !update.getMessage().hasText()){
            return null;
        }

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();
        // Получаем пользователя из БД или создаем нового. Нужно знать BotState
        User user = userService.getOrCreateUser(chatId, message.getFrom().getUserName());

        // Обработка команд (Команды имеют приоритет над состояниями)
        if ("/start".equals(text)){
            userService.updateBotState(chatId, BotState.IDLE);
            return handlers.get(BotState.IDLE).handle(message);
        }

        if("/schedule".equals(text)){
            return scheduleStartHandler.handle(message);
        }
        if ("/ai".equals(text)) {
            userService.updateBotState(chatId, BotState.WAITING_FOR_AI_QUESTION);
            return new SendMessage(chatId.toString(), "Задавай любой вопрос нейросети:");
        }
        if ("/dota".equals(text)) {
            userService.updateBotState(chatId, BotState.WAITING_FOR_DOTA_ID);
            return new SendMessage(chatId.toString(), "Пришли свой Dota 2 Account ID (цифры):");
        }
        if ("/list".equals(text)) { return scheduleListHandler.handle(message); }

        // 3. Обработка по состояниям (Если это не команда, значит это ответ пользователя на вопрос бота)
        // Узнаем, что мы ждем от юзера
        BotState currentState = user.getBotState();
        // Ищем хендлер для этого ожидания
        InputMessageHandler handler = handlers.get(currentState);

        // Если хендлер не найден
        if(handler == null){
            log.warn("Handler for state {} not found", currentState );
            return new SendMessage(chatId.toString(), "неизвестная команда или состояние");
        }
        // Вызываем логику обработки и возвращаем результат боту
        return handler.handle(message);
    }
}
