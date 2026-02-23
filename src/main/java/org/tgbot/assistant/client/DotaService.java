package org.tgbot.assistant.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.tgbot.assistant.dto.DotaResponseDto;

@Service
public class DotaService {

    private final RestClient restClient;

    public DotaService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.opendota.com/api")
                .build();
    }

    public DotaResponseDto getPlayerStats(Long accountId) {
        try {
            return restClient.get()
                    .uri("/players/" + accountId)
                    .retrieve()
                    .body(DotaResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }


    public String getRankName(Integer tier) {
        if (tier == null) return "Неизвестный ранг (профиль скрыт)";
        String[] ranks = {"Uncalibrated", "Herald", "Guardian", "Crusader", "Archon", "Legend", "Ancient", "Divine", "Immortal"};
        int mainRank = tier / 10;
        int stars = tier % 10;
        return ranks[mainRank] + " [" + stars + "]";
    }
}
