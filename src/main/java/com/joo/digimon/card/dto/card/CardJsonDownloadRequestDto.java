package com.joo.digimon.card.dto.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardJsonDownloadRequestDto {
    private String prefix;           // BT1, ST1, EX1, P 등
    private String color;            // RED, BLUE, YELLOW, GREEN, BLACK, PURPLE, WHITE (프로모가 아닌 경우)
    private Integer startNumber;     // 프로모(P)인 경우 시작 번호
    private Integer endNumber;       // 프로모(P)인 경우 종료 번호
}
