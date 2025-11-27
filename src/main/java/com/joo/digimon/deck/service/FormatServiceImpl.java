package com.joo.digimon.deck.service;

import com.joo.digimon.card.model.NoteEntity;
import com.joo.digimon.card.repository.NoteRepository;
import com.joo.digimon.deck.dto.FormatRequestDto;
import com.joo.digimon.deck.dto.FormatResponseDto;
import com.joo.digimon.deck.dto.FormatUpdateRequestDto;
import com.joo.digimon.deck.model.Format;
import com.joo.digimon.deck.repository.FormatRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormatServiceImpl implements FormatService {

    private final FormatRepository formatRepository;
    private final NoteRepository noteRepository;

    @PostConstruct
    @Transactional
    public void init() {
        Optional<Format> optionalFormat = formatRepository.findById(1);
        if (optionalFormat.isPresent()) {
            return;
        }
        formatRepository.save(Format.builder()
                .name("ALL")
                .startDate(LocalDate.of(1998, 7, 15))
                .endDate(LocalDate.of(9999, 12, 31))
                .isOnlyEn(false)
                .build());
    }

    @Override
    @Transactional
    public void createFormat(FormatRequestDto formatRequestDto) {
        // NoteEntity 조회 (선택적)
        NoteEntity noteEntity = null;
        if (formatRequestDto.getNoteId() != null) {
            noteEntity = noteRepository.findById(formatRequestDto.getNoteId()).orElse(null);
        }

        formatRepository.save(
                Format.builder()
                        .name(formatRequestDto.getFormatName())
                        .startDate(formatRequestDto.getStartDate())
                        .endDate(formatRequestDto.getEndDate())
                        .isOnlyEn(formatRequestDto.getIsOnlyEn())
                        .noteEntity(noteEntity)
                        .build()
        );
    }

    @Override
    public List<FormatResponseDto> getFormatList(LocalDate latestReleaseCardDate) {
        List<FormatResponseDto> result = new ArrayList<>();

        Sort sort = Sort.by("endDate").descending();
        List<Format> formats = formatRepository.findByEndDateGreaterThanEqual(latestReleaseCardDate, sort);

        for (Format format : formats) {
            result.add(new FormatResponseDto(format));
        }
        return result;
    }

    @Override
    public List<FormatResponseDto> getAllFormat() {
        List<FormatResponseDto> result = new ArrayList<>();
        List<Format> formats = formatRepository.findAll();
        for (Format format : formats) {
            result.add(new FormatResponseDto(format));
        }
        return result;
    }

    @Override
    @Transactional
    public void updateFormat(List<FormatUpdateRequestDto> dtos) {
        for (FormatUpdateRequestDto dto : dtos) {
            Format format = formatRepository.findById(dto.getId()).orElseThrow();
            format.update(dto);

            // NoteEntity 연결 업데이트 (선택적)
            if (dto.getNoteId() != null) {
                NoteEntity noteEntity = noteRepository.findById(dto.getNoteId()).orElse(null);
                format.updateNoteEntity(noteEntity);
            } else {
                // noteId가 명시적으로 null인 경우 연결 해제
                format.updateNoteEntity(null);
            }
        }
    }

    @Override
    public FormatResponseDto getCurrentFormat() {
        Format format = formatRepository.findTopByIsOnlyEnIsNullOrIsOnlyEnIsFalseOrderByStartDateDesc().orElseThrow();
        return new FormatResponseDto(format);
    }

    @Override
    public void deleteFormat(Integer formatId) {
        formatRepository.deleteById(formatId);
    }
}
