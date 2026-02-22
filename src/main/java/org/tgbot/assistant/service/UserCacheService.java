package org.tgbot.assistant.service;

import org.springframework.stereotype.Service;
import org.tgbot.assistant.entity.Schedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserCacheService {
    private final Map<Long, Schedule> scheduleCache = new ConcurrentHashMap<>();

    public void saveDraft(Long userId, Schedule draft){
        scheduleCache.put(userId, draft);
    }

    public Schedule getDraft(Long userId){
        return scheduleCache.getOrDefault(userId, new Schedule());
    }

    public void clear(Long userId){
        scheduleCache.remove(userId);
    }

}
