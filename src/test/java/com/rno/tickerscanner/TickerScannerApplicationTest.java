package com.rno.tickerscanner;

import com.rno.tickerscanner.utils.SeleniumUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class TickerScannerApplicationTest extends BaseTest {

    @Test
    @SneakyThrows
    public void untitled() {

        WebDriver webDriver = SeleniumUtils.getWebDriver();
        webDriver.get("http://localhost:8080/");
        webDriver.manage().window().setSize(new Dimension(2005, 1039));
        webDriver.findElement(By.name("patternName")).click();
        {
            WebElement dropdown = webDriver.findElement(By.name("patternName"));
            dropdown.findElement(By.xpath("//option[. = 'DOJIDAY1']")).click();
        }
        webDriver.findElement(By.name("tradeDirection")).click();
        {
            WebElement dropdown = webDriver.findElement(By.name("tradeDirection"));
            dropdown.findElement(By.xpath("//option[. = 'SHORT']")).click();
        }
        webDriver.findElement(By.name("initialEquity")).clear();
        webDriver.findElement(By.name("initialEquity")).sendKeys("50000");


        webDriver.findElement(By.name("capitalPerTradePct")).clear();
        webDriver.findElement(By.name("capitalPerTradePct")).sendKeys("0.2");
        webDriver.findElement(By.name("tradeStopType")).click();
        {
            WebElement dropdown = webDriver.findElement(By.name("tradeStopType"));
            dropdown.findElement(By.xpath("//option[. = 'DAY_1_HIGH']")).click();
        }
        webDriver.findElement(By.cssSelector("form")).click();
        webDriver.findElement(By.name("maxLossDollar")).clear();
        webDriver.findElement(By.name("maxLossDollar")).sendKeys("0");
        webDriver.findElement(By.name("maxLossPct")).clear();
        webDriver.findElement(By.name("maxLossPct")).sendKeys("0");
        webDriver.findElement(By.cssSelector("form")).click();
        webDriver.findElement(By.name("maxDaysToHold")).clear();
        webDriver.findElement(By.name("maxDaysToHold")).sendKeys("5");



        webDriver.findElement(By.cssSelector("form")).click();
        webDriver.findElement(By.cssSelector("input:nth-child(1)")).click();

        TimeUnit.SECONDS.sleep(60l);
    }
}
