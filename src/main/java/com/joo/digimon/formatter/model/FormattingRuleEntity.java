package com.joo.digimon.formatter.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 텍스트 포맷팅 규칙 엔티티
 */
@Entity
@Table(name = "formatting_rule")
@Getter
@Setter
@NoArgsConstructor
public class FormattingRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 규칙 타입 (EXACT_MATCH, BRACKET_CONTENT, TEXT_REPLACE)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RuleType type;

    /**
     * 원본 패턴 (검색할 텍스트)
     */
    @Column(nullable = false, length = 500)
    private String pattern;

    /**
     * 교체될 텍스트
     */
    @Column(nullable = false, length = 500)
    private String replacement;

    /**
     * 우선순위 (높을수록 먼저 적용)
     * EXACT_MATCH: 100, BRACKET_CONTENT: 50, TEXT_REPLACE: 10 권장
     */
    @Column(nullable = false)
    private Integer priority = 0;

    /**
     * 활성화 여부
     */
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * 규칙 설명
     */
    @Column(length = 1000)
    private String description;

    /**
     * 생성일시
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
