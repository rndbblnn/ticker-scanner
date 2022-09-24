package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.dao.entity.PatternMatchEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PatternMatchRepository extends CrudRepository<PatternMatchEntity, Long> {

    boolean existsByPatternNameEqualsAndSymbolEqualsAndPatternTimeEqualsAllIgnoreCase(String patternName,
                                                                                      String symbol,
                                                                                      LocalDateTime time);

    List<PatternMatchEntity> findByPatternName(String patternName);

    List<PatternMatchEntity> findByPatternNameOrderByPatternTimeDesc(String patternName);

}