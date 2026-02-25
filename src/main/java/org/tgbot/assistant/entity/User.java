package org.tgbot.assistant.entity;

import jakarta.persistence.*;
import lombok.*;

// Отображение таблицы в виде класса

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tg_id", nullable = false, unique = true)
    private  Long tgId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "bot_state")
    // Аннотация, чтобы спринг сохранял enum не как числа
    @Enumerated(EnumType.STRING)
    private BotState botState;
}
