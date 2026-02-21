package org.tgbot.assistant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
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
