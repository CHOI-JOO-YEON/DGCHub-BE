package com.joo.digimon.formatter.controller;

import com.joo.digimon.formatter.dto.CreateFormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDto;
import com.joo.digimon.formatter.dto.FormattingRuleDictionaryDto;
import com.joo.digimon.formatter.model.RuleType;
import com.joo.digimon.formatter.service.FormattingRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 포맷팅 규칙 관리 Controller (Admin 전용)
 */
@RestController
@RequestMapping({"/api/admin/formatting-rules", "/admin-api/admin/formatting-rules"})  // 두 경로 모두 지원
@RequiredArgsConstructor
public class FormattingRuleController {

    private final FormattingRuleService formattingRuleService;

    /**
     * 규칙 목록 조회 (타입별 필터링 가능)
     *
     * @param type 규칙 타입 (EXACT_MATCH, BRACKET_CONTENT, TEXT_REPLACE)
     * @return 규칙 목록
     */
    @GetMapping
    public ResponseEntity<List<FormattingRuleDto>> getRules(
            @RequestParam(required = false) RuleType type
    ) {
        List<FormattingRuleDto> rules = formattingRuleService.getRules(type);
        return ResponseEntity.ok(rules);
    }

    /**
     * 특정 규칙 조회
     *
     * @param id 규칙 ID
     * @return 규칙 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormattingRuleDto> getRule(@PathVariable Long id) {
        FormattingRuleDto rule = formattingRuleService.getRule(id);
        return ResponseEntity.ok(rule);
    }

    /**
     * 규칙 생성
     *
     * @param dto 생성할 규칙 정보
     * @return 생성된 규칙
     */
    @PostMapping
    public ResponseEntity<FormattingRuleDto> createRule(
            @Valid @RequestBody CreateFormattingRuleDto dto
    ) {
        FormattingRuleDto created = formattingRuleService.createRule(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 규칙 수정
     *
     * @param id  규칙 ID
     * @param dto 수정할 규칙 정보
     * @return 수정된 규칙
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormattingRuleDto> updateRule(
            @PathVariable Long id,
            @Valid @RequestBody CreateFormattingRuleDto dto
    ) {
        FormattingRuleDto updated = formattingRuleService.updateRule(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 규칙 삭제
     *
     * @param id 규칙 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        formattingRuleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 규칙 일괄 생성
     *
     * @param dtos 생성할 규칙 목록
     * @return 생성된 규칙 목록
     */
    @PostMapping("/batch")
    public ResponseEntity<List<FormattingRuleDto>> createRulesBatch(
            @Valid @RequestBody List<CreateFormattingRuleDto> dtos
    ) {
        List<FormattingRuleDto> created = formattingRuleService.createRulesBatch(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 활성화된 규칙 딕셔너리 조회 (클라이언트용)
     * 클라이언트에서 포맷팅 로직에 사용
     *
     * @return 규칙 딕셔너리 (exactMatch, bracketContent, textReplace)
     */
    @GetMapping("/dictionary")
    public ResponseEntity<FormattingRuleDictionaryDto> getDictionary() {
        FormattingRuleDictionaryDto dictionary = formattingRuleService.getActiveDictionary();
        return ResponseEntity.ok(dictionary);
    }

    /**
     * 규칙 내보내기 (JSON Export)
     * 활성화된 규칙만 딕셔너리 형태로 내보내기
     *
     * @return 규칙 딕셔너리
     */
    @GetMapping("/export")
    public ResponseEntity<FormattingRuleDictionaryDto> exportRules() {
        FormattingRuleDictionaryDto dictionary = formattingRuleService.exportRuleDictionary();
        return ResponseEntity.ok(dictionary);
    }
}
