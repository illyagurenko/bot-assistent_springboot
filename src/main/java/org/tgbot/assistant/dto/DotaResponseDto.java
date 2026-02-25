package org.tgbot.assistant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// Класс для обработки ответа API OpenDota

@Data
public class DotaResponseDto {

    @JsonProperty("rank_tier")
    private Integer rankTier;

    // Вложенный объект profile. В JSON это выглядит как "profile": { ... }
    private Profile profile;

    // Вложенный статический класс для маппинга подобъекта profile
    @Data
    public static class Profile {
        @JsonProperty("personaname")
        private String personaName;
    }
}