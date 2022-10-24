package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.stonks.commons.entity.CandleDailyEntity;
import com.rndbblnn.tickerscanner.BaseIntegrationTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
