package com.joo.digimon.card.model;

import com.joo.digimon.global.enums.Attribute;
import com.joo.digimon.global.enums.CardType;
import com.joo.digimon.global.enums.Color;
import com.joo.digimon.global.enums.Form;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TOKENS_TB")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String cardNo;

    @Enumerated(EnumType.STRING)
    CardType cardType;

    Integer lv;
    Integer dp;
    Integer playCost;

    @Enumerated(EnumType.STRING)
    Color color1;
    @Enumerated(EnumType.STRING)
    Color color2;
    @Enumerated(EnumType.STRING)
    Color color3;

    @Enumerated(EnumType.STRING)
    Form form;

    @Enumerated(EnumType.STRING)
    Attribute attribute;

    @Column(columnDefinition = "TEXT")
    String types;

    String korName;
    String engName;
    String jpnName;

    @Column(length = 1000)
    String korEffect;
    @Column(length = 1000)
    String engEffect;
    @Column(length = 1000)
    String jpnEffect;

    @Column(length = 1000)
    String korSourceEffect;
    @Column(length = 1000)
    String engSourceEffect;
    @Column(length = 1000)
    String jpnSourceEffect;

    String imgUrl;
    String smallImgUrl;

    public void updateCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void updateLv(Integer lv) {
        this.lv = lv;
    }

    public void updateDp(Integer dp) {
        this.dp = dp;
    }

    public void updatePlayCost(Integer playCost) {
        this.playCost = playCost;
    }

    public void updateColors(Color color1, Color color2, Color color3) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    public void updateForm(Form form) {
        this.form = form;
    }

    public void updateAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void updateTypes(String types) {
        this.types = types;
    }

    public void updateKorName(String korName) {
        this.korName = korName;
    }

    public void updateEngName(String engName) {
        this.engName = engName;
    }

    public void updateJpnName(String jpnName) {
        this.jpnName = jpnName;
    }

    public void updateKorEffect(String korEffect) {
        this.korEffect = korEffect;
    }

    public void updateEngEffect(String engEffect) {
        this.engEffect = engEffect;
    }

    public void updateJpnEffect(String jpnEffect) {
        this.jpnEffect = jpnEffect;
    }

    public void updateKorSourceEffect(String korSourceEffect) {
        this.korSourceEffect = korSourceEffect;
    }

    public void updateEngSourceEffect(String engSourceEffect) {
        this.engSourceEffect = engSourceEffect;
    }

    public void updateJpnSourceEffect(String jpnSourceEffect) {
        this.jpnSourceEffect = jpnSourceEffect;
    }

    public void updateImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void updateSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }
}
