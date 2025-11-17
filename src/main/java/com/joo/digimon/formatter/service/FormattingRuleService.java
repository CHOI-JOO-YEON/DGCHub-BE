package com.joo.digimon.formatter.service;

import com.joo.digimon.formatter.dto.CreateFormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDictionaryDto;
import com.joo.digimon.formatter.model.RuleType;

import java.util.List;

/**
 * 포맷팅 규칙 Service Interface
 */
public interface FormattingRuleService {

    /**
     * 모든 규칙 조회 (타입 필터링 가능)
     */
    List<FormattingRuleDto> getRules(RuleType type);

    /**
     * 특정 규칙 조회
     */
    FormattingRuleDto getRule(Long id);

    /**
     * 규칙 생성
     */
    FormattingRuleDto createRule(CreateFormattingRuleDto dto);

    /**
     * 규칙 수정
     */
    FormattingRuleDto updateRule(Long id, CreateFormattingRuleDto dto);

    /**
     * 규칙 삭제
     */
    void deleteRule(Long id);

    /**
     * 규칙 일괄 생성
     */
    List<FormattingRuleDto> createRulesBatch(List<CreateFormattingRuleDto> dtos);

    /**
     * 활성화된 규칙 딕셔너리 조회 (클라이언트용)
     */
    FormattingRuleDictionaryDto getActiveDictionary();

    /**
     * 전체 규칙 딕셔너리 조회 (Export용)
     */
    FormattingRuleDictionaryDto exportRuleDictionary();
}
