package com.joo.digimon.card.dto;

import com.joo.digimon.card.model.CardEntity;
import com.joo.digimon.card.model.CardImgEntity;
import com.joo.digimon.card.model.EnglishCardEntity;
import com.joo.digimon.card.model.JapaneseCardEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class SimpleCardData {
    String cardNo;
    String name;
    String effect;
    String sourceEffect;
    String imageUrl;
    List<String> traits;

    public SimpleCardData(CardImgEntity cardImg, String prefixUrl) {
        CardEntity cardEntity = cardImg.getCardEntity();
        this.cardNo = cardEntity.getCardNo();
        this.name = cardEntity.getCardName();
        this.effect = cardEntity.getEffect();
        this.sourceEffect = cardEntity.getSourceEffect();
        this.imageUrl = prefixUrl + getImageUrlByCardImg(cardImg);
        traits = new ArrayList<>();
        if (cardEntity.getForm() != null) {
            var form = cardEntity.getForm().getKor();
            if (form.contains("/")) {
                Collections.addAll(traits, form.split("/"));
            } else {
                traits.add(form);
            }
        }

        if (cardEntity.getAttribute() != null) {
            traits.add(cardEntity.getAttribute().getKor());
        }
        cardEntity
                .getCardCombineTypeEntities()
                .forEach(cardCombineTypeEntity ->
                        traits.add(cardCombineTypeEntity.getTypeEntity().getName()));


    }

    private String getImageUrlByCardImg(CardImgEntity cardImg) {
        return Optional.ofNullable(cardImg.getBigWebpUrl())
                .or(() -> Optional.ofNullable(cardImg.getCardEntity().getEnglishCard())
                        .map(EnglishCardEntity::getWebpUrl))
                .or(() -> Optional.ofNullable(cardImg.getCardEntity().getEnglishCard())
                        .map(EnglishCardEntity::getSampleWebpUrl))
                .or(() -> Optional.ofNullable(cardImg.getCardEntity().getJapaneseCardEntity())
                        .map(JapaneseCardEntity::getWebpUrl))
                .orElse(null);
    }

}
