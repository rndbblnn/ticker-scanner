package com.rno.tickertracker;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TickerTrackerApplication.class,
//        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class BaseTest {

}
