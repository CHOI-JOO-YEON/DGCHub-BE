package com.joo.digimon.card.dto.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joo.digimon.global.enums.Attribute;
import com.joo.digimon.global.enums.CardType;
import com.joo.digimon.global.enums.Color;
import com.joo.digimon.global.enums.Form;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UpdateTokenDto {
    String cardNo;
    CardType cardType;
    Integer lv;
    Integer dp;
    Integer playCost;
    Color color1;
    Color color2;
    Color color3;
    Form form;
    Attribute attribute;
    List<String> types;
    String korName;
    String engName;
    String jpnName;
    String korEffect;
    String engEffect;
    String jpnEffect;
    String korSourceEffect;
    String engSourceEffect;
    String jpnSourceEffect;
}
