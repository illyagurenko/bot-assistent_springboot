package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import java.time.DayOfWeek;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.service.UserCacheService;
import org.tgbot.assistant.service.UserService;

/**
 * Класс реализует промежуточный шаг сложной транзакции. Мы не можем создать запись в БД сразу, так как у нас нет всех данных (времени, названия).
 * Поэтому этот файл берет необходимые данные (день недели) и передает по состоянию в следующие хендлеры.
 */

// Аннотация для превращения класса в бин для спринга
@Component
//Генерирует конструктор для всех final-полей
@RequiredArgsConstructor
public class ScheduleDayHandler implements InputMessageHandler{

    private final UserService userService;
    private final UserCacheService userCacheService;

    // Логика обработки ответа пользователя
    @Override
    public SendMessage handle(Message message) {
        // Приводим текст к верхнему регистру для Enum DayOfWeek
        String text = message.getText().toUpperCase();
        Long chatId = message.getChatId();

        try{
            // Приводим к типу DayOfWeek
            DayOfWeek day = DayOfWeek.valueOf(text);

            // Достаем из временной памяти (Map) расписания для этого пользователя
            Schedule draft = userCacheService.getDraft(chatId);
            // Записываем выбранный день
            draft.setDayOfWeek(day);
            // Сохраняем обратно
            userCacheService.saveDraft(chatId, draft);
            // Меняем статус BotState
            userService.updateBotState(chatId, BotState.WAITING_FOR_SCHEDULE_TIME);

            return new SendMessage(chatId.toString(), " День записан: " + day + "\nТеперь введите время урока (формат HH:mm):");

        }
        catch (IllegalArgumentException e){
            // Если ввели неправильный день недели
            return new SendMessage(chatId.toString(), "Непонятный день недели! \nПожалуйста, введите на английском: MONDAY, TUESDAY, WEDNESDAY...");
        }
    }
    // Хендлер слушает сообщения только тогда, когда пользователь находится в состоянии WAITING_FOR_SCHEDULE_DAY
    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_SCHEDULE_DAY;
    }
}
