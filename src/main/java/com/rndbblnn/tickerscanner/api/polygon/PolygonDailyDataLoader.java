package com.rndbblnn.tickerscanner.api.polygon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PolygonDailyDataLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PolygonDailyDataLoader.class);
//
//    @Autowired
//    private PolygonRestClient polygonRestClient;
//
//    @Autowired
//    private TickerRepository tickerRepository;
//
//    public void saveDailyDateRange(LocalDate start, LocalDate end) {
//
//        LocalDate currentDate = end;
//
//        while (currentDate.isAfter(start)) {
//
//            final String currentDateStr = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
//
//            LOGGER.info("Fetching data for {}", currentDateStr);
//
//            AggregatesDTO aggregatesDTO =
//                    PolygonApi.exec(() ->
//                            polygonRestClient.getGroupedDailyAggregatesBlocking(
//                                    new GroupedDailyParameters(
//                                            "us",
//                                            "stocks",
//                                            currentDateStr,
//                                            false
//                                    )
//                            )
//                    );
//
//            aggregatesDTO.getResults().stream()
//                    .filter(aggregateDTO ->
//                    !tickerRepository.existsTickerOhlcEntityByTimeAndSymbol(
//                            LocalDateTime.ofInstant(
//                                    Instant.ofEpochMilli(aggregateDTO.getTimestampMillis()),
//                                    ZoneId.systemDefault()),
//                            aggregateDTO.getTicker()
//                    ))
//                    .forEach(aggregateDTO -> {
//                        tickerRepository.save(
//                                new TickerOhlcEntity()
//                                        .setSymbol(aggregateDTO.getTicker())
//                                        .setTime(
//                                                LocalDateTime.ofInstant(
//                                                        Instant.ofEpochMilli(aggregateDTO.getTimestampMillis()),
//                                                        ZoneId.systemDefault())
//                                        )
//                                        .setOpen(aggregateDTO.getOpen())
//                                        .setHigh(aggregateDTO.getHigh())
//                                        .setLow(aggregateDTO.getLow())
//                                        .setClose(aggregateDTO.getClose())
//                                        .setVolume(aggregateDTO.getVolume().longValue())
//                        );
//                    });
//
//
//            currentDate = DateUtils.minusMarketDays(currentDate, 1);
//        }
//
//
//    }

}
