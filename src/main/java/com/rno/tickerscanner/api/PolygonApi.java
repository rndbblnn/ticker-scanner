package com.rno.tickerscanner.api;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class PolygonApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonApi.class);

    private static List<LocalDateTime> callQueue = new ArrayList<>(10);

    @SneakyThrows
    public static <T> T exec(Supplier f) {

        while (true) {
            Iterator<LocalDateTime> it = callQueue.iterator();
            while (it.hasNext()) {
                LocalDateTime localDateTime = it.next();
                if (localDateTime.isBefore(LocalDateTime.now().minus(60, ChronoUnit.SECONDS))) {
                    it.remove();
                }
            }
            if (callQueue.size() >= 5) {
                LOGGER.info("Sleeping {} seconds...", 10);
                TimeUnit.SECONDS.sleep(10);
            }
            else {
                break;
            }
        }

        T res = (T) f.get();

        callQueue.add(LocalDateTime.now());

        return res;
    }

//    public static <T> T exec(Supplier f) throws InterruptedException {
//
//        if (lastCall != null) {
//            int seconds = (int) ChronoUnit.SECONDS.between(LocalDate.now(), lastCall);
//            if (seconds < 6) {
//                LOGGER.info("Sleeping {} seconds...", seconds);
//                TimeUnit.SECONDS.sleep(seconds);
//            }
//        }
//
//        T res = (T) f.get();
//
//        lastCall = LocalDate.now();
//
//        return res;
//    }

}
