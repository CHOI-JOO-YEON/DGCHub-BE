package com.joo.digimon.card.repository;

import com.joo.digimon.card.model.CardEntity;
import com.joo.digimon.global.enums.CardType;
import com.joo.digimon.global.enums.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<CardEntity,Integer>{
    Optional<CardEntity> findByCardNo(String cardNo);

    List<CardEntity> findByCardTypeAndIsOnlyEnCardIsNullOrIsOnlyEnCardIsFalse(CardType cardType);

    List<CardEntity> findByCardNoIn(Collection<String> cardNos);

    Page<CardEntity> findAll(Pageable pageable);

    @Query("SELECT DISTINCT c FROM CardEntity c " +
           "LEFT JOIN FETCH c.cardCombineTypeEntities cct " +
           "LEFT JOIN FETCH cct.typeEntity " +
           "WHERE c.cardNo LIKE CONCAT(:prefix, '-%') " +
           "AND (c.color1 = :color OR c.color2 = :color OR c.color3 = :color)")
    List<CardEntity> findByPrefixAndColor(@Param("prefix") String prefix, @Param("color") Color color);

    @Query("SELECT DISTINCT c FROM CardEntity c " +
           "LEFT JOIN FETCH c.cardCombineTypeEntities cct " +
           "LEFT JOIN FETCH cct.typeEntity " +
           "WHERE c.cardNo IN :cardNos")
    List<CardEntity> findByCardNoInWithTypes(@Param("cardNos") Collection<String> cardNos);
}
