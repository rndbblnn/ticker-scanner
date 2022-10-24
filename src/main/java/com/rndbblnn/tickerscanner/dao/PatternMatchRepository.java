package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.dao.entity.PatternMatchEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface PatternMatchRepository extends CrudRepository<PatternMatchEntity, Long> {

    boolean existsByPatternNameEqualsAndSymbolEqualsAndPatternTimeEqualsAllIgnoreCase(String patternName,
                                                                                      String symbol,
                                                                                      LocalDateTime time);

    List<PatternMatchEntity> findByPatternName(String patternName);

    List<PatternMatchEntity> findByPatternNameOrderByPatternTimeDesc(String patternName);

}