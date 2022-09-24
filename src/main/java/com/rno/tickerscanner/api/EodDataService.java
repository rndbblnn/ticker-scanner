package com.rno.tickerscanner.api;

import com.rno.tickerscanner.utils.SeleniumUtils;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EodDataService {

    private static Iterator<String> it = null;
    private static int RETRY_COUNT = 20;
    private static final long MAX_DOWNLOAD_WAIT = TimeUnit.SECONDS.toMillis(60);

    @SneakyThrows
    public List<File> fetchOHLCV(List<String> symbolList) {


        if (it == null) {
            it = symbolList.iterator();
        }

        WebDriver driver = SeleniumUtils.getWebDriver(true);

        try {
            login(driver);

            while (it.hasNext()) {

                String symbol = it.next();

                download(driver, symbol);

                System.out.println("\t" + symbol + ": done.");
            }
        }
        catch (Exception e) {
            SeleniumUtils.closeDriver(driver);
            if (RETRY_COUNT-- > 0) {
                this.fetchOHLCV(symbolList);
            }
            else {
                throw e;
            }
        }
        finally {
            try {
                driver.quit();
            } catch (Exception e) {
            }
            try {
                driver.close();
            } catch (Exception e) {
            }
        }

        return null;
    }

    private void login(WebDriver driver) {
        driver.get("https://www.eoddata.com/");

        driver.findElement(By.id("ctl00_cph1_lg1_txtEmail")).sendKeys("rndbblnn");
        driver.findElement(By.id("ctl00_cph1_lg1_txtPassword")).sendKeys("YPMh6q3h!8biLQs");
        driver.findElement(By.id("ctl00_cph1_lg1_btnLogin")).click();
    }

    private boolean download(WebDriver driver, String symbol) {
        driver.get("https://www.eoddata.com/");

        WebElement searchBoxElement =
                SeleniumUtils.waitAndFind(driver, By.id("ctl00_Menu1_s1_txtSearch"));

        searchBoxElement.clear();
        searchBoxElement.sendKeys(symbol);
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        }
        catch (InterruptedException e) {
        }

        WebElement searchTable = null;
        try {
            searchTable = SeleniumUtils.waitAndFind(driver, By.className("SearchPane_Content"));
        } catch (NoSuchElementException e) {
            return true;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
        }

        for (WebElement row : searchTable.findElements(By.tagName("tr"))) {
            List<WebElement> tdList = row.findElements(By.tagName("td"));

            String tableSymbol = tdList.get(0).getText();
            String tableExchange = tdList.get(2).getText();

            if (symbol.matches(tableSymbol)
                    && tableExchange.matches("NYSE|AMEX|NASDAQ|OTCBB")) {

                System.out.println("found " + tableExchange + ": " + tableSymbol);

                row.findElement(By.tagName("td")).click();
                new Select(SeleniumUtils.waitAndFind(driver, By.id("ctl00_cph1_dd1_cboPeriod"))).selectByValue("2");
                driver.findElement(By.id("ctl00_cph1_dd1_btnDownload")).click();

                long waitTime = 0;
                while (!new File(SeleniumUtils.FIREFOX_DOWNLOAD_DIR + symbol + "_20170102_20220315.txt").exists()) {

                    if (waitTime > MAX_DOWNLOAD_WAIT) {
                        login(driver);
                        download(driver, symbol);
                        break;
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(250);
                    } catch (InterruptedException e) {
                    }
                    waitTime += 250;
                }
                break;
            }
        }
        return false;
    }


}
