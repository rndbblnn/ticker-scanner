package com.rndbblnn.tickerscanner;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@ComponentScan("com.rndbblnn")
@EntityScan("com.rndbblnn.*")
@EnableScheduling
@Slf4j
public class TickerScannerApplication {

  public static void main(String... strings) {
        SpringApplication.run(TickerScannerApplication.class, strings);
  }

  @Scheduled(cron = "0 * * * * *")
  @SneakyThrows
  public void deleteGradleLogs() {
    Files.find(Paths.get("C:\\Users\\"+ System.getProperty("user.name") + "\\.gradle\\daemon"),
            Integer.MAX_VALUE,
            (filePath, fileAttr) -> fileAttr.isRegularFile())
        .filter(f -> f.toFile().getName().endsWith(".log"))
        .forEach(f -> {
          try {
            f.toFile().delete();
          } catch (Exception e) {
          }
        });
  }

}
