package com.rno.tickertracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TickerTrackerApplication {

    public static void main(String... strings) {
        SpringApplication.run(TickerTrackerApplication.class, strings);
    }

}
