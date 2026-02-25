package org.tgbot.assistant.service;

import org.springframework.stereotype.Service;
import org.tgbot.assistant.entity.Schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс решает проблему короткой памяти запросов. Когда бот спрашивает «Какой день?», а потом «Какое время?», между этими
 * событиями приложение забывает всё. Этот сервис позволяет склеить несколько сообщений в один полноценный объект Schedule.
 */

// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
public class UserCacheService {

    // Реализация кеша (черновик расписания или карта)
    private final Map<Long, Schedule> scheduleCache = new ConcurrentHashMap<>();

    // Сохраняет или обновляет черновик для конкретного пользователя
    public void saveDraft(Long userId, Schedule draft){
        scheduleCache.put(userId, draft);
    }

    // Достает черновик из памяти или создает новый
    public Schedule getDraft(Long userId){
        return scheduleCache.getOrDefault(userId, new Schedule());
    }

    //Удаляет данные из памяти, когда расписание уже сохраненно в реальное БД
    public void clear(Long userId){
        scheduleCache.remove(userId);
    }

}
