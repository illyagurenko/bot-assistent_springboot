package org.tgbot.assistant.entity;


import jakarta.persistence.*;
import lombok.*;



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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "dota_account_id")
    private Long dotaAccountId;
}
