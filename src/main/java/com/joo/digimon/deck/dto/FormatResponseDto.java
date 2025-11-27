package com.joo.digimon.deck.dto;

import com.joo.digimon.deck.model.Format;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FormatResponseDto {
    Integer id;
    String formatName;
    LocalDate startDate;
    LocalDate endDate;
    Boolean isOnlyEn;
    Integer noteId;
    String noteName;

    public FormatResponseDto(Format format) {
        this.id = format.getId();
        this.formatName = format.getName();
        this.startDate = format.getStartDate();
        this.endDate = format.getEndDate();
        this.isOnlyEn = format.getIsOnlyEn();

        // NoteEntity 정보 추가
        if (format.getNoteEntity() != null) {
            this.noteId = format.getNoteEntity().getId();
            this.noteName = format.getNoteEntity().getName();
        }
    }
}
