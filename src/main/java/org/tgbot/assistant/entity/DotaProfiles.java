package org.tgbot.assistant.entity;


import jakarta.persistence.*;
import lombok.*;

// Отображение таблицы в виде класса

@Entity
@Table(name = "dota_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DotaProfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Один аккаунт для одного пользователя, также ленивая загрузка, как в schedule
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "dota_account_id")
    private Long dotaAccountId;
}
