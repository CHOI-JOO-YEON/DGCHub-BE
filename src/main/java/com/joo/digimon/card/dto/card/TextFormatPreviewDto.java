package com.joo.digimon.card.dto.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TextFormatPreviewDto {
    private Integer cardId;
    private String cardNo;
    private String cardName;
    private Boolean hasChanges;
    private Boolean effectChanged;
    private Boolean sourceEffectChanged;
    private String originalEffect;
    private String formattedEffect;
    private String originalSourceEffect;
    private String formattedSourceEffect;
}
