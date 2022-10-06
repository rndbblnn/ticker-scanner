package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.BaseIntegrationTest;
import org.assertj.core.util.Streams;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CassandraUtilsDao extends BaseIntegrationTest {

    @Autowired
    private PatternMatchRepository patternMatchRepository;

    @Test
    public void deletePatternMatchByPatternNane() {

//        String patternName = PatternEnum.STAIR_UP_GAP_UP.name();

        String patternName = "JJJJUNIT";

        Streams.stream(patternMatchRepository.findAll())
                .filter(patternMatchEntity -> patternMatchEntity.getPatternName().matches(patternName))
                .forEach(patternMatchEntity -> {
                    patternMatchRepository.delete(patternMatchEntity);
                });
    }


}
