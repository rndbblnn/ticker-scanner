package com.rno.tickerscanner.utils;

import org.junit.Test;

import java.time.LocalDate;

public class ScreenshotUtilsTest {

    @Test
    public void testGetScreenshotIntraday() {

        String base64str = SeleniumUtils.getScreenshotIntraday(
                "TSLA",
                LocalDate.of(2017, 01, 05));

        System.out.println(base64str);
    }

    @Test
    public void testGetScreenshotTradingView() {

        String base64str = SeleniumUtils.getScreenshotTradingView(
                "TSLA",
                LocalDate.of(2017, 01, 05));

        System.out.println(base64str);
    }


}
