package com.joo.digimon.card.dto.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joo.digimon.card.dto.card.LocaleCardData;
import com.joo.digimon.card.model.TokenEntity;
import com.joo.digimon.global.enums.CardType;
import com.joo.digimon.global.enums.Color;
import com.joo.digimon.global.enums.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenVo {
    Integer tokenId;
    String cardNo;
    CardType cardType;
    Boolean isToken;
    Integer lv;
    Integer dp;
    Integer playCost;
    Color color1;
    Color color2;
    Color color3;
    List<LocaleCardData> localeCardData;

    public TokenVo(TokenEntity tokenEntity, String prefixUrl) {
        this.tokenId = tokenEntity.getId();
        this.cardNo = tokenEntity.getCardNo();
        this.cardType = tokenEntity.getCardType();
        this.isToken = true;
        this.lv = tokenEntity.getLv();
        this.dp = tokenEntity.getDp();
        this.playCost = tokenEntity.getPlayCost();
        this.color1 = tokenEntity.getColor1();
        this.color2 = tokenEntity.getColor2();
        this.color3 = tokenEntity.getColor3();

        localeCardData = new ArrayList<>();

        if (tokenEntity.getKorName() != null) {
            String imgUrl = tokenEntity.getImgUrl() != null ? prefixUrl + tokenEntity.getImgUrl() : null;
            String smallImgUrl = tokenEntity.getSmallImgUrl() != null ? prefixUrl + tokenEntity.getSmallImgUrl() : null;

            localeCardData.add(new LocaleCardData(
                    tokenEntity.getKorName(),
                    tokenEntity.getKorEffect(),
                    tokenEntity.getKorSourceEffect(),
                    Locale.KOR,
                    imgUrl,
                    smallImgUrl
            ));
        }

        if (tokenEntity.getEngName() != null) {
            String imgUrl = tokenEntity.getImgUrl() != null ? prefixUrl + tokenEntity.getImgUrl() : null;
            String smallImgUrl = tokenEntity.getSmallImgUrl() != null ? prefixUrl + tokenEntity.getSmallImgUrl() : null;

            localeCardData.add(new LocaleCardData(
                    tokenEntity.getEngName(),
                    tokenEntity.getEngEffect(),
                    tokenEntity.getEngSourceEffect(),
                    Locale.ENG,
                    imgUrl,
                    smallImgUrl
            ));
        }

        if (tokenEntity.getJpnName() != null) {
            String imgUrl = tokenEntity.getImgUrl() != null ? prefixUrl + tokenEntity.getImgUrl() : null;
            String smallImgUrl = tokenEntity.getSmallImgUrl() != null ? prefixUrl + tokenEntity.getSmallImgUrl() : null;

            localeCardData.add(new LocaleCardData(
                    tokenEntity.getJpnName(),
                    tokenEntity.getJpnEffect(),
                    tokenEntity.getJpnSourceEffect(),
                    Locale.JPN,
                    imgUrl,
                    smallImgUrl
            ));
        }
    }
}
