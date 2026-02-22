package org.tgbot.assistant.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.repository.ScheduleRepository;
import org.tgbot.assistant.repository.UserRepository;
import org.tgbot.assistant.service.UserCacheService;
import org.tgbot.assistant.service.UserService;

@Component
@RequiredArgsConstructor
public class ScheduleTitleHandler implements InputMessageHandler{

    private final UserService userService;
    private final UserCacheService userCacheService;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Override
    public SendMessage handle(Message message) {
        String title = message.getText();
        Long chatId = message.getChatId();

        Schedule draft = userCacheService.getDraft(chatId);

        User user = userRepository.findByTgId(chatId).orElseThrow();

        draft.setTitle(title);
        draft.setUser(user);
        draft.setIsActive(true);

        scheduleRepository.save(draft);

        userCacheService.clear(chatId);
        userService.updateBotState(chatId, BotState.IDLE);

        return new SendMessage(chatId.toString(), "Расписание сохранено. \nЯ напомню тебе о занятии '" + title + "' в " + draft.getTime());
    }

    @Override
    public BotState getHandlerName() {
        return BotState.WAITING_FOR_SCHEDULE_TITLE;
    }
}
