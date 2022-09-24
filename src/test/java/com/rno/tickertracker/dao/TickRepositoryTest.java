package com.rno.tickertracker.dao;

import com.rno.tickertracker.BaseTest;
import com.rno.tickertracker.dao.entity.TickEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TickRepositoryTest extends BaseTest {

    @Autowired
    private TickRepository tickRepository;

    @Test
    public void testSave() {

        LocalDateTime tickTime = LocalDate.now().atTime(16,0,0);

        for (int i = 0 ; i < 20; i++) {
            tickRepository.save(new TickEntity()
                    .setTimeframe("T")
                    .setSymbol("TEST" + RandomStringUtils.randomAlphabetic(5))
                    .setTickTime(tickTime)
            );
        }


    }
}
