package com.joo.digimon.formatter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.joo.digimon.formatter.model.RuleType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 포맷팅 규칙 Response DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormattingRuleDto {
    private Long id;
    private RuleType type;
    private String pattern;
    private String replacement;
    private Integer priority;
    private Boolean active;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
