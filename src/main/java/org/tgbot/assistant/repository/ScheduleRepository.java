package org.tgbot.assistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tgbot.assistant.entity.Schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDayOfWeekAndTimeAndIsActiveTrue(DayOfWeek dayOfWeek, LocalTime time);
}
