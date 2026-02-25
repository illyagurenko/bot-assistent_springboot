package org.tgbot.assistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.assistant.entity.Schedule;
import org.tgbot.assistant.entity.User;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Интерфейс служит мостом между кодом и таблицами
 */

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // Поиск для уведомлений
    List<Schedule> findByDayOfWeekAndTimeAndIsActiveTrue(DayOfWeek dayOfWeek, LocalTime time);
    // Получение всего списка для пользователя
    List<Schedule> findAllByUserOrderByDayOfWeekAscTimeAsc(User user);
}
