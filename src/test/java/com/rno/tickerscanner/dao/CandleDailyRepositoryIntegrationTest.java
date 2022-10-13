package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.BaseIntegrationTest;
import com.rno.tickerscanner.dao.entity.CandleDailyEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CandleDailyRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CandleDailyRepository candleDailyRepository;

    @Test
    public void testSave() {

        LocalDateTime tickTime = LocalDate.now().atTime(16,0,0);

        for (int i = 0 ; i < 20; i++) {
            candleDailyRepository.save((CandleDailyEntity) new CandleDailyEntity()
                    .setSymbol("TEST" + RandomStringUtils.randomAlphabetic(5))
                    .setTickTime(tickTime)
            );
        }


    }
}
