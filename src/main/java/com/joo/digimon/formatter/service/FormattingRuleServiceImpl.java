package com.joo.digimon.formatter.service;

import com.joo.digimon.formatter.dto.CreateFormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDictionaryDto;
import com.joo.digimon.formatter.model.FormattingRuleEntity;
import com.joo.digimon.formatter.model.RuleType;
import com.joo.digimon.formatter.repository.FormattingRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 포맷팅 규칙 Service 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormattingRuleServiceImpl implements FormattingRuleService {

    private final FormattingRuleRepository repository;

    @Override
    public List<FormattingRuleDto> getRules(RuleType type) {
        List<FormattingRuleEntity> entities;

        if (type != null) {
            entities = repository.findByTypeOrderByPriorityDescIdAsc(type);
        } else {
            entities = repository.findAll();
        }

        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FormattingRuleDto getRule(Long id) {
        FormattingRuleEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("규칙을 찾을 수 없습니다. ID: " + id));

        return toDto(entity);
    }

    @Override
    @Transactional
    public FormattingRuleDto createRule(CreateFormattingRuleDto dto) {
        // 중복 체크
        repository.findByPatternAndType(dto.getPattern(), dto.getType())
                .ifPresent(rule -> {
                    throw new RuntimeException("동일한 패턴과 타입의 규칙이 이미 존재합니다: " + dto.getPattern());
                });

        FormattingRuleEntity entity = new FormattingRuleEntity();
        entity.setType(dto.getType());
        entity.setPattern(dto.getPattern());
        entity.setReplacement(dto.getReplacement());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : getDefaultPriority(dto.getType()));
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        entity.setDescription(dto.getDescription());

        FormattingRuleEntity saved = repository.save(entity);
        return toDto(saved);
    }

    @Override
    @Transactional
    public FormattingRuleDto updateRule(Long id, CreateFormattingRuleDto dto) {
        FormattingRuleEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("규칙을 찾을 수 없습니다. ID: " + id));

        // 패턴이나 타입이 변경되는 경우 중복 체크
        if (!entity.getPattern().equals(dto.getPattern()) || entity.getType() != dto.getType()) {
            repository.findByPatternAndType(dto.getPattern(), dto.getType())
                    .ifPresent(rule -> {
                        if (!rule.getId().equals(id)) {
                            throw new RuntimeException("동일한 패턴과 타입의 규칙이 이미 존재합니다: " + dto.getPattern());
                        }
                    });
        }

        entity.setType(dto.getType());
        entity.setPattern(dto.getPattern());
        entity.setReplacement(dto.getReplacement());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : getDefaultPriority(dto.getType()));
        entity.setActive(dto.getActive() != null ? dto.getActive() : true);
        entity.setDescription(dto.getDescription());

        FormattingRuleEntity updated = repository.save(entity);
        return toDto(updated);
    }

    @Override
    @Transactional
    public void deleteRule(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("규칙을 찾을 수 없습니다. ID: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public List<FormattingRuleDto> createRulesBatch(List<CreateFormattingRuleDto> dtos) {
        return dtos.stream()
                .map(this::createRule)
                .collect(Collectors.toList());
    }

    @Override
    public FormattingRuleDictionaryDto getActiveDictionary() {
        List<FormattingRuleEntity> rules = repository.findByActiveTrueOrderByPriorityDescIdAsc();
        return buildDictionary(rules);
    }

    @Override
    public FormattingRuleDictionaryDto exportRuleDictionary() {
        List<FormattingRuleEntity> rules = repository.findByActiveTrue();
        return buildDictionary(rules);
    }

    /**
     * Entity -> DTO 변환
     */
    private FormattingRuleDto toDto(FormattingRuleEntity entity) {
        FormattingRuleDto dto = new FormattingRuleDto();
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setPattern(entity.getPattern());
        dto.setReplacement(entity.getReplacement());
        dto.setPriority(entity.getPriority());
        dto.setActive(entity.getActive());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    /**
     * 규칙 리스트를 딕셔너리로 변환
     */
    private FormattingRuleDictionaryDto buildDictionary(List<FormattingRuleEntity> rules) {
        Map<String, String> exactMatch = new HashMap<>();
        Map<String, String> bracketContent = new HashMap<>();
        Map<String, String> textReplace = new HashMap<>();

        for (FormattingRuleEntity rule : rules) {
            switch (rule.getType()) {
                case EXACT_MATCH:
                    exactMatch.put(rule.getPattern(), rule.getReplacement());
                    break;
                case BRACKET_CONTENT:
                    bracketContent.put(rule.getPattern(), rule.getReplacement());
                    break;
                case TEXT_REPLACE:
                    textReplace.put(rule.getPattern(), rule.getReplacement());
                    break;
            }
        }

        FormattingRuleDictionaryDto dto = new FormattingRuleDictionaryDto();
        dto.setExactMatch(exactMatch);
        dto.setBracketContent(bracketContent);
        dto.setTextReplace(textReplace);

        return dto;
    }

    /**
     * 타입별 기본 우선순위 반환
     */
    private Integer getDefaultPriority(RuleType type) {
        switch (type) {
            case EXACT_MATCH:
                return 100;
            case BRACKET_CONTENT:
                return 50;
            case TEXT_REPLACE:
                return 10;
            default:
                return 0;
        }
    }
}
