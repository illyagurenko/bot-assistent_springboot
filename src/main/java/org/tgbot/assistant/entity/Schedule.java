package org.tgbot.assistant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

// Отображение таблицы в виде класса

@Entity
@Table(name = "schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Связь Многие к Одному.  Говорит о том, что много записей расписания могут принадлежать одному пользователю
    //fetch = FetchType.LAZY - Ленивая загрузка. Hibernate не будет доставать данные о пользователе из базы до тех пор, пока ты явно не вызовешь метод getUser()
    @ManyToOne(fetch = FetchType.LAZY)
    //Указывает на колонку в таблице schedules, которая является внешним ключом к таблице users
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "notification_time")
    private LocalTime time;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name="is_active")
    private Boolean isActive;
}
