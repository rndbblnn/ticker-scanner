package com.rndbblnn.tickerscanner.utils;

import java.text.DecimalFormat;

public class NumberUtils {

    private static final DecimalFormat DECIMAL_FORMAT_2DIGITS = new DecimalFormat("#.##");
    private static final String[] CURRENCY_SUFFIXES = {"", "K", "M", "B", "T", "P", "E"};

    public static final String prettyPrint(float value) {
        String arr[] = CURRENCY_SUFFIXES;
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        return String.format("%s %s", DECIMAL_FORMAT_2DIGITS.format(value), arr[index]);
    }

}
