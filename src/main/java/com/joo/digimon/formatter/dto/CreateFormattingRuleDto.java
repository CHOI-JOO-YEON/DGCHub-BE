package com.joo.digimon.formatter.dto;

import com.joo.digimon.formatter.model.RuleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 포맷팅 규칙 생성/수정 Request DTO
 */
@Data
public class CreateFormattingRuleDto {

    @NotNull(message = "규칙 타입은 필수입니다")
    private RuleType type;

    @NotBlank(message = "패턴은 필수입니다")
    @Size(max = 500, message = "패턴은 500자 이내여야 합니다")
    private String pattern;

    @NotBlank(message = "교체값은 필수입니다")
    @Size(max = 500, message = "교체값은 500자 이내여야 합니다")
    private String replacement;

    private Integer priority = 0;

    private Boolean active = true;

    @Size(max = 1000, message = "설명은 1000자 이내여야 합니다")
    private String description;
}
