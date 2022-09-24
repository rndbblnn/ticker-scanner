package com.rno.tickerscanner.utils;

import com.rno.tickerscanner.TickerScannerConstants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class DateUtils {

    public static final DateTimeFormatter YEARMONTH_DATEFORMAT = DateTimeFormatter.ofPattern("YY-MMM");
    public static final Comparator<LocalDateTime> LOCAL_DATE_COMPARATOR_ASC = new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o1.compareTo(o2);
        }
    };
    public static final Comparator<LocalDateTime> LOCAL_DATE_COMPARATOR_DESC = new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o2.compareTo(o1);
        }
    };

    public static LocalDate minusMarketDays(LocalDate localDate, long days) {

        LocalDate localDateCopy = LocalDate.from(localDate);

        for (int i = 0; i < days; i++) {

            localDateCopy = localDateCopy.minusDays(1);

            while (!isMarketDay(localDateCopy)) {
                localDateCopy = localDateCopy.minusDays(1);
            }
        }

        return localDateCopy;
    }

    public static LocalDate plusMarketDays(LocalDate localDate, long days) {

        LocalDate localDateCopy = LocalDate.from(localDate);

        for (int i = 0; i < days; i++) {

            localDateCopy = localDateCopy.plusDays(1);

            while (!isMarketDay(localDateCopy)) {
                localDateCopy = localDateCopy.plusDays(1);
            }
        }

        return localDateCopy;
    }

    public static boolean isMarketDay(LocalDate localDate) {
        return localDate.getDayOfWeek() != DayOfWeek.SATURDAY
                && localDate.getDayOfWeek() != DayOfWeek.SUNDAY
                && !TickerScannerConstants.STOCK_MARKET_HOLIDAYS.contains(localDate);
    }

    public static long getMarketDaysBetween(LocalDate localDate1, LocalDate localDate2) {
        long cpt = 1;

        LocalDate currentDate = localDate1;
        while (currentDate.isBefore(localDate2)) {
            if (isMarketDay(currentDate)) {
                cpt++;
            }
            currentDate = plusMarketDays(currentDate, 1);
        }
        return cpt;
    }



    public static void main(String... strings) {


        System.out.println(

                getMarketDaysBetween(
                        LocalDate.of(2021,1,1),
                        LocalDate.of(2021, 12, 31)
                )

        );


    }
}
