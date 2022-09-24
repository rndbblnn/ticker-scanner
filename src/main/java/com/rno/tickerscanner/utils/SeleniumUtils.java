package com.rno.tickerscanner.utils;

import com.rno.tickerscanner.TickerScannerConstants;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SeleniumUtils {

    public static final String FIREFOX_DOWNLOAD_DIR = "D:\\_selenium\\";

    private static WebDriver driver;

    static {
        System.setProperty("java.awt.headless", "false");
        System.setProperty("webdriver.chrome.driver", TickerScannerConstants.PATH_TO_CHROMEDRIVER_X64);
        System.setProperty("webdriver.gecko.driver", TickerScannerConstants.PATH_TO_GECKO_X64);
    }

    public static WebDriver getWebDriver(boolean standalone) {
        if (!standalone && driver != null) {
            return driver;
        }
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--user-data-dir=C:/Users/" + System.getProperty("user.name") + "/AppData/Local/Google/Chrome/User Data");
//        chromeOptions.addArguments("--user-data-dir=C:/Users/selenium/AppData/Local/Google/Chrome/User Data");
//        chromeOptions.setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--disable-dev-shm-usage");
//        driver = new ChromeDriver(chromeOptions);

        FirefoxProfile profile =
                new FirefoxProfile(new File("C:\\Users\\water\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\1wi32p8c.default-release"));

        // Instructing firefox to use custom download location
        profile.setPreference("browser.download.folderList", 2);

        // Setting custom download directory
        profile.setPreference("browser.download.dir", FIREFOX_DOWNLOAD_DIR);

        FirefoxOptions options = new FirefoxOptions()
                .setProfile(profile)
                .addPreference("browser.helperApps.neverAsk.saveToDisk",
                        "text/plain; charset=utf-8; text/csv;application/json;charset=utf-8;application/pdf;text/plain;application/text;text/xml;application/xml");
        ;

        WebDriver driver = new FirefoxDriver(options);
        if (!standalone) {
            SeleniumUtils.driver = driver;
        }

//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    public static WebDriver getWebDriver() {
        return getWebDriver(false);
    }


    @SneakyThrows
    public static String getScreenshotIntraday(String symbol, LocalDate localDate) {
        getWebDriver().get("https://www.barchart.com/stocks/quotes/" + symbol + "/interactive-chart");
        driver.manage().window().setSize(new Dimension(1805, 1130));

        //driver.findElement(By.cssSelector("linkText=Weekly")).click();

        driver.findElement(By.cssSelector(".calendar")).click();
//        driver.findElement(By.cssSelector(".column > .bc-dropdown > .ng-pristine")).click();

//        driver.findElement(By.cssSelector(".interactive-chart-field-minutes__fieldset > .ng-untouched")).click();
//        driver.findElement(By.cssSelector(".interactive-chart-field-minutes__fieldset > .ng-untouched")).sendKeys("5");
//        driver.findElement(By.cssSelector(".interactive-chart-date-range:nth-child(1) > .bc-datepicker-item:nth-child(1) > .bc-glyph-calendar")).click();
//        driver.findElement(By.cssSelector(".interactive-chart-date-range:nth-child(1) > .bc-datepicker-item:nth-child(1) > .bc-glyph-calendar")).click();
//        driver.findElement(By.cssSelector(".interactive-chart-date-range:nth-child(1) > .bc-datepicker-item:nth-child(1) > .ng-pristine")).click();

//        TimeUnit.SECONDS.sleep(3);

        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(1) > div > i"))
                .click();
        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(1) > div > input"))
                .clear();
        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(1) > div > input"))
                .sendKeys(DateUtils.minusMarketDays(localDate, 1).format(DateTimeFormatter.ofPattern("MM/DD/YYYY")));
//        TimeUnit.SECONDS.sleep(3);

        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > i"))
                .click();
        //click on datepicker
        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > input"))
                .click();

        // click on random date
        driver.findElement(By.cssSelector("body > div.bc-datepicker.am-fade.bc-datepicker-mode-0.bottom-left > table > tbody > tr:nth-child(3) > td:nth-child(3) > button > span"))
                .click();
        //click on datepicker
        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > i"))
                .click();


        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > input"))
                .clear();

//        WebElement webElement = driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > input"));
//        Lists.charactersOf(
//                DateUtils.plusMarketDays(localDate,10).format(DateTimeFormatter.ofPattern("MM/DD/YYYY")))
//                .forEach(s -> {
//                    webElement.sendKeys(String.valueOf(s));
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(10l);
//                    } catch (InterruptedException e) {
//                    }
//                });

        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > input"))
                .sendKeys(DateUtils.plusMarketDays(localDate, 10).format(DateTimeFormatter.ofPattern("MM/DD/YYYY")));

//        for (int i = 0; i<10; i++) {
//            driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > form > div.show-for-medium-up.for-tablet-and-desktop > div:nth-child(3) > div > input"))
//                    .sendKeys(Keys.ARROW_RIGHT);
//        }

        driver.findElement(By.cssSelector(" body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > div")).click();


        driver.findElement(By.cssSelector("body > div.reveal-modal.fade.interactive-chart-modal-aggregation.in > div > div > div > button.bc-button.light-blue")).click();
        // js.executeScript("window.scrollTo(0,769)");
        //js.executeScript("window.scrollTo(0,400)");

        TimeUnit.SECONDS.sleep(5);

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }


    @SneakyThrows
    public static String getScreenshotTradingView(String symbol, LocalDate localDate) {


        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--user-data-dir=C:/Users/" + System.getProperty("user.name") + "/AppData/Local/Google/Chrome/User Data");
        driver = getWebDriver();
        driver.get("https://www.tradingview.com/chart/4XnZg27q/");

        try {
            System.out.println("Waiting for Alert");
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.alertIsPresent()).dismiss();
            System.out.println("Alert Displayed");
        } catch (Exception e) {
            System.out.println("Alert not Displayed");
        }

        driver.manage().window().setSize(new Dimension(2545, 1380));

        String dateIsoStr = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        driver.findElement(By.cssSelector("#header-toolbar-symbol-search > .js-button-text")).click();
        driver.findElement(By.cssSelector(".search-Hsmn_0WX")).sendKeys(symbol);
        driver.findElement(By.cssSelector(".search-Hsmn_0WX")).sendKeys(Keys.ENTER);
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
        }
        driver.findElement(By.cssSelector(".chart-container:nth-child(2) tr:nth-child(1) .chart-gui-wrapper > canvas:nth-child(2)")).click();

        driver.findElement(By.cssSelector(".icon-yLOygoSG > svg")).click();
        for (int i = 0; i < 10; i++) {
            driver.findElement(By.cssSelector(".intent-primary-1uA2IWJE .input-3bEGcMc9")).sendKeys(Keys.BACK_SPACE);
        }
        driver.findElement(By.cssSelector(".intent-primary-1uA2IWJE .input-3bEGcMc9")).sendKeys(dateIsoStr);
        driver.findElement(By.cssSelector(".submitButton-KW8170fm .content-1UNGmyXO")).click();
        driver.findElement(By.cssSelector(".chart-container:nth-child(3) tr:nth-child(1) .chart-gui-wrapper > canvas:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".icon-yLOygoSG")).click();
        driver.findElement(By.cssSelector(".submitButton-KW8170fm .content-1UNGmyXO")).click();
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
        }

//                    try {
//                        FileUtils.copyFile(
//                                ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
//                                new File("output/screenshot"+ dateIsoStr + symbol + ".png"));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);

    }

    public static final boolean isAlive(Optional<String> url) {
        try {
            String currentUrl = driver.getCurrentUrl();
            if (url.isPresent()) {
                return currentUrl.matches(url + ".*");
            }
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    @SneakyThrows
    public static final WebElement waitAndFind(WebDriver driver, By by) {
        for (int i = 0; i < 20; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(250);
                return driver.findElement(by);
            } catch (Exception e) {
            }
        }
        return driver.findElement(by);
    }

    public static final void closeDriver() {
        closeDriver(driver);
        driver = null;
    }

    public static final void closeDriver(WebDriver driver) {
        try {
            driver.quit();
        } catch (Exception e) {
        }
        try {
            driver.close();
        } catch (Exception e) {
        }
        driver = null;
    }

}
