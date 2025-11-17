package com.joo.digimon.formatter.repository;

import com.joo.digimon.formatter.model.FormattingRuleEntity;
import com.joo.digimon.formatter.model.RuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 포맷팅 규칙 Repository
 */
@Repository
public interface FormattingRuleRepository extends JpaRepository<FormattingRuleEntity, Long> {

    /**
     * 활성화된 규칙 전체 조회 (우선순위 순)
     */
    List<FormattingRuleEntity> findByActiveTrueOrderByPriorityDescIdAsc();

    /**
     * 타입별 활성화된 규칙 조회
     */
    List<FormattingRuleEntity> findByTypeAndActiveTrueOrderByPriorityDescIdAsc(RuleType type);

    /**
     * 타입별 규칙 조회 (활성/비활성 무관)
     */
    List<FormattingRuleEntity> findByTypeOrderByPriorityDescIdAsc(RuleType type);

    /**
     * 패턴과 타입으로 규칙 조회 (중복 체크용)
     */
    Optional<FormattingRuleEntity> findByPatternAndType(String pattern, RuleType type);

    /**
     * 활성화된 규칙만 조회
     */
    List<FormattingRuleEntity> findByActiveTrue();

    /**
     * 타입별 활성화된 규칙 조회 (단순)
     */
    List<FormattingRuleEntity> findByTypeAndActiveTrue(RuleType type);
}
