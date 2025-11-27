package com.joo.digimon.deck.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FormatUpdateRequestDto {
    Integer id;
    String formatName;
    LocalDate startDate;
    LocalDate endDate;
    Boolean isOnlyEn;
    Integer noteId;  // 선택적: NoteEntity 연결 (null이면 연결 해제)
}
