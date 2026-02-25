package org.tgbot.assistant.service.handler;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.tgbot.assistant.entity.BotState;


/**
 * Интерфейс реализует принцип Полиморфизма. В UpdateDispatcher мы работаем с типом InputMessageHandler.
 * Это позволяет нам добавлять в бота большое количество новых функций, не меняя код диспетчера.
 * Мы создаем новый класс, который реализует этот интерфейс, и Spring сам подхватит его.
 */

public interface InputMessageHandler {
    /**
     * Основной метод для обработки входящего сообщения.
     * message - объект сообщения от пользователя (текст, id чата и т.д.)
     * SendMessage - объект ответа, который бот отправит пользователю.
     */
    SendMessage handle(Message message);
    /**
     * Метод-идентификатор.
     * BotState - состояние бота, за которое отвечает конкретный хендлер.
     * По этому значению UpdateDispatcher находит нужный хендлер в своей карте (Map).
     */
    BotState getHandlerName();
}
