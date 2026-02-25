package org.tgbot.assistant.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tgbot.assistant.dto.DotaResponseDto;

/**
 * Сервис для запросов к OpenDotaAPI
 */

// Аннотация, помечающая класс как сервис с бизнес-логикой
@Service
public class DotaService {

    private final RestClient restClient;

    // Конструктор класса
    public DotaService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.opendota.com/api")
                .build();
    }

    // Получает данные профиля игрока из OpenDota
    public DotaResponseDto getPlayerStats(Long accountId) {
        try {
            // Делаем GET запрос
            return restClient.get()
                    // Дописываем путь к базовому URL
                    .uri("/players/" + accountId)
                    .retrieve()
                    .body(DotaResponseDto.class);// Превращаем JSON в наш Java-объект
        } catch (Exception e) {
            return null;
        }
    }

    // Метод-конвертер из числа в ранг
    public String getRankName(Integer tier) {
        if (tier == null) return "Неизвестный ранг (профиль скрыт)";
        // Массив названий рангов
        String[] ranks = {"Uncalibrated", "Herald", "Guardian", "Crusader", "Archon", "Legend", "Ancient", "Divine", "Immortal"};
        int mainRank = tier / 10;
        int stars = tier % 10;
        return ranks[mainRank] + " [" + stars + "]";
    }
}
