package com.joo.digimon.formatter.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 클라이언트용 포맷팅 규칙 딕셔너리 DTO
 */
@Data
public class FormattingRuleDictionaryDto {

    /**
     * 완전 일치 규칙 맵
     * key: 원본 패턴, value: 교체값
     */
    private Map<String, String> exactMatch = new HashMap<>();

    /**
     * 괄호 내용 기반 규칙 맵
     * key: 괄호 내용, value: 교체값
     */
    private Map<String, String> bracketContent = new HashMap<>();

    /**
     * 단순 텍스트 교체 규칙 맵
     * key: 원본 텍스트, value: 교체값
     */
    private Map<String, String> textReplace = new HashMap<>();
}
