package com.joo.digimon.card.dto.card;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardJsonResponseDto {
    private String name;
    private String form;
    private Integer level;
    private List<String> color;
    private String attribute;
    private String rarity;

    @JsonProperty("card_type")
    private String cardType;

    @JsonProperty("play_cost")
    private Integer playCost;

    private String type;
    private Integer dp;

    @JsonProperty("digivolve_conditions")
    private List<DigivolveCondition> digivolveConditions;

    private List<Effect> effects;

    @Getter
    @Builder
    public static class DigivolveCondition {
        private Integer cost;
        private Integer level;
        private String color;
    }

    @Getter
    @Builder
    public static class Effect {
        @JsonProperty("is_inherited")
        private Boolean isInherited;
        private String text;
    }
}
