package com.rno.tickertracker.dao;

import com.rno.tickertracker.BaseTest;
import org.assertj.core.util.Streams;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CassandraUtilsDao extends BaseTest {

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
