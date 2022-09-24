package com.rno.tickerscanner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TickerScannerConstants {

    public static final DateTimeFormatter US_DATE_FORMAT = DateTimeFormatter.ofPattern("M-d-yy");
    public static final DateTimeFormatter US_NO_DASH_DATE_FORMAT = DateTimeFormatter.ofPattern("Mdyy");

    public static final Set<LocalDate> STOCK_MARKET_HOLIDAYS = new HashSet<>();
    static {
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 1, 1));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 1, 16));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 2, 20));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 4, 14));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 5, 29));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 7, 4));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 9, 4));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 11, 23));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2017, 12, 25));

        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 1, 1));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 1, 15));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 2, 19));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 3, 30));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 5, 28));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 7, 4));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 9, 3));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 11, 22));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 12, 5));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2018, 12, 25));

        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 1, 1));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 1, 21));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 2, 18));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 4, 19));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 5, 27));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 7, 4));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 9, 2));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 11, 28));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2019, 12, 25));

        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 1, 1));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 1, 20));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 2, 17));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 4, 10));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 5, 25));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 7, 3));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 9, 7));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 11, 26));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2020, 12, 25));

        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 1, 1));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 1, 18));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 2, 15));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 4, 2));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 5, 31));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 7, 5));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 9, 6));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 11, 25));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2021, 12, 24));

        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 1, 17));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 2, 21));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 4, 15));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 5, 30));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 6, 20));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 7, 4));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 9, 5));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 11, 24));
        STOCK_MARKET_HOLIDAYS.add(LocalDate.of(2022, 12, 26));
    }

    public static void main(String... strings) {
        System.out.println(STOCK_MARKET_HOLIDAYS.contains(LocalDate.of(2021, 1, 1)));
        System.out.println(STOCK_MARKET_HOLIDAYS.contains(LocalDate.of(2021, 1, 2)));
    }
}
