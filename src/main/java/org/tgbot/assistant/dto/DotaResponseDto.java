package org.tgbot.assistant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DotaResponseDto {

    @JsonProperty("rank_tier")
    private Integer rankTier;

    private Profile profile;

    @Data
    public static class Profile {
        @JsonProperty("personaname")
        private String personaName;
    }
}