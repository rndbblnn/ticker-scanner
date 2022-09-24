package com.rno.tickerscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TickerScannerApplication {

    public static void main(String... strings) {
        SpringApplication.run(TickerScannerApplication.class, strings);
    }

}
