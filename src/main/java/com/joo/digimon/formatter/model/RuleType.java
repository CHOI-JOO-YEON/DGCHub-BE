package com.joo.digimon.formatter.model;

/**
 * 포맷팅 규칙 타입
 */
public enum RuleType {
    /**
     * 완전 일치 규칙 (최우선)
     * 예: "【어택 시】" -> "【어택 시】"
     */
    EXACT_MATCH,

    /**
     * 괄호 내용 기반 규칙
     * 예: "어택 시" -> "【어택 시】"
     */
    BRACKET_CONTENT,

    /**
     * 단순 텍스트 교체 (최후순위)
     * 예: "1체" -> "1마리"
     */
    TEXT_REPLACE
}
