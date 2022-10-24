package com.rno.tickerscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.rndbblnn")
@EntityScan("com.rndbblnn.*")
public class TickerScannerApplication {

    public static void main(String... strings) {
        SpringApplication.run(TickerScannerApplication.class, strings);
    }

}
