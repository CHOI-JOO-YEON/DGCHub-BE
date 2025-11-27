package com.joo.digimon.deck.model;

import com.joo.digimon.card.model.NoteEntity;
import com.joo.digimon.deck.dto.FormatRequestDto;
import com.joo.digimon.deck.dto.FormatUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "FORMATS_TB")
public class Format {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    Boolean isOnlyEn;
    LocalDate startDate;
    LocalDate endDate;

    @OneToMany(mappedBy = "format")
    Set<DeckEntity> decks;

    @OneToOne
    @JoinColumn(name = "note_entity_id")
    NoteEntity noteEntity;

    public void update(FormatUpdateRequestDto dto){
        this.name = dto.getFormatName();
        this.isOnlyEn = dto.getIsOnlyEn();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
    }

    public void updateStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
