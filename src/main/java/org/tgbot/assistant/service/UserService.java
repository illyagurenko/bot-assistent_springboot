package org.tgbot.assistant.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tgbot.assistant.entity.BotState;
import org.tgbot.assistant.entity.User;
import org.tgbot.assistant.repository.UserRepository;

/**
 * Класс инкапсулирует (скрывает) логику работы с таблицей пользователей, в хендлерах мы используем его для работы с БД
 */
// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
//Генерирует конструктор для всех final-полей
@RequiredArgsConstructor
public class UserService {

    // Репозиторий — это наш прямой канал связи с таблицей users
    private final UserRepository userRepository;

    // Если внутри что-то пойдет не так, Transactional отменит все изменения в базе
    @Transactional
    public User getOrCreateUser(Long tgId, String username){
        // Ищем пользователя по его Telegram ID
        return userRepository.findByTgId(tgId)
                .orElseGet(() ->{
                    User newUser = User.builder()
                            .tgId(tgId)
                            .username(username)
                            .botState(BotState.IDLE)
                            .build();
                    // Сохраняем нового человечка в базу, если не нашли
                    return userRepository.save(newUser);
                        });
    }

    // Метод для изменения BotState
    @Transactional
    public void updateBotState(Long tgId, BotState newState){
        // Ищем юзера в базе
        userRepository.findByTgId(tgId).ifPresent(user -> {
            // Меняем состояние
            user.setBotState(newState);
            // Сохраняем
            userRepository.save(user);
        });
    }
}
