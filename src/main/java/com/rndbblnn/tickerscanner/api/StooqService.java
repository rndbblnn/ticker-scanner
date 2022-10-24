package com.rndbblnn.tickerscanner.api;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.rndbblnn.stonks.commons.entity.CandleDailyEntity;
import com.rndbblnn.tickerscanner.dao.CandleDailyRepository;
import com.rndbblnn.tickerscanner.dao.PatternMatchRepository;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StooqService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StooqService.class);

    @Autowired
    private CandleDailyRepository candleDailyRepository;

    @Autowired
    private PatternMatchRepository patternMatchRepository;

    @SneakyThrows
    public void persistFile(File file) {

        File usStockFile = new File(file.getAbsolutePath() + "\\data\\daily\\us");

        for (File f : usStockFile.listFiles()) {

            LOGGER.info("Processing folder... {}", f.getAbsolutePath());

//            if (f.getName().matches("nasdaq etfs")) {
//                LOGGER.info("Skipping {}", f.getName());
//                continue;
//            }

            for (File csvFile : f.listFiles()) {

                processFile(csvFile);

            }
        }
    }

    private void processFile(File csvFile) throws IOException, CsvValidationException {

        if (csvFile.isDirectory()) {
            LOGGER.info("\tProcessing folder... {}", csvFile.getAbsolutePath());

            for (File f : csvFile.listFiles()) {
                processFile(f);
            }
            return;
        }

        LOGGER.info("\t\tProcessing file... {}", csvFile.getAbsolutePath());

        Reader reader = null;
        CSVReader csvReader = null;

        try {
            try {
                reader = Files.newBufferedReader(csvFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            csvReader = new CSVReader(reader);

            String[] line;
            csvReader.readNext();
            long dayCounter = 0;

            while ((line = csvReader.readNext()) != null) {
                CandleDailyEntity candleDailyEntity =
                    (CandleDailyEntity) new CandleDailyEntity()
                                .setSymbol(line[0].replaceAll("\\.US", ""))
                                .setTickTime(LocalDateTime.of(
                                        Integer.valueOf(line[2].substring(0, 4)),
                                        Integer.valueOf(line[2].substring(4, 6)),
                                        Integer.valueOf(line[2].substring(6, 8)),
                                        16,
                                        0
                                ))
                                .setOpen(Double.valueOf(line[4]))
                                .setHigh(Double.valueOf(line[5]))
                                .setLow(Double.valueOf(line[6]))
                                .setClose(Double.valueOf(line[7]))
                                .setVolume(Long.valueOf(line[8]));

//                if (!patternMatchRepository.existsByPatternNameEqualsAndSymbolEqualsAllIgnoreCase(
//                        PatternEnum.BREAKOUT_30D_MAX_HIGH.name(),
//                        tickerOhlcEntity.getSymbol()
//                )) {
//                    return;
//                }

                if (candleDailyEntity.getTickTime().isBefore(LocalDateTime.now().minus(6, ChronoUnit.YEARS))) {
//                    LOGGER.info("\t\t\t Skipping old shit {}", tickerOhlcEntity.getTime());
                } else {
                    Optional<CandleDailyEntity> tickerOhlcEntityOptional =
                            candleDailyRepository.findBySymbolAndTickTime(
                                    candleDailyEntity.getSymbol(),
                                    candleDailyEntity.getTickTime()
                            );

                    if (tickerOhlcEntityOptional.isPresent()) {
//                        LOGGER.info("\t\t\t\tSkipping...{}", tickerOhlcEntity.toString());

//                                if (!tickerOhlcEntityOptional.get().getOpen().equals(tickerOhlcEntity.getOpen())) {
//                                    System.err.println("Discrepancy found OPEN : " +
//                                            tickerOhlcEntityOptional.get().getOpen() + " -> " + tickerOhlcEntity.getOpen());
//                                    tickerOhlcEntityOptional.get().setOpen(tickerOhlcEntity.getOpen());
//                                }
//                                if (!tickerOhlcEntityOptional.get().getHigh().equals(tickerOhlcEntity.getHigh())) {
//                                    System.err.println("Discrepancy found HIGH : " +
//                                            tickerOhlcEntityOptional.get().getHigh() + " -> " + tickerOhlcEntity.getHigh());
//                                    tickerOhlcEntityOptional.get().setHigh(tickerOhlcEntity.getHigh());
//                                }
//                                if (!tickerOhlcEntityOptional.get().getLow().equals(tickerOhlcEntity.getLow())) {
//                                    System.err.println("Discrepancy found  LOW : " +
//                                            tickerOhlcEntityOptional.get().getLow() + " -> " + tickerOhlcEntity.getLow());
//                                    tickerOhlcEntityOptional.get().setLow(tickerOhlcEntity.getLow());
//                                }
//                                if (!tickerOhlcEntityOptional.get().getClose().equals(tickerOhlcEntity.getClose())) {
//                                    System.err.println("Discrepancy found CLOSE: " +
//                                            tickerOhlcEntityOptional.get().getClose() + " -> " + tickerOhlcEntity.getClose());
//                                    tickerOhlcEntityOptional.get().setClose(tickerOhlcEntity.getClose());
//                                }
//                                if (!tickerOhlcEntityOptional.get().getVolume().equals(tickerOhlcEntity.getVolume())) {
//                                    System.err.println("Discrepancy found  VOL : " +
//                                            tickerOhlcEntityOptional.get().getVolume() + " -> " + tickerOhlcEntity.getVolume());
//                                    tickerOhlcEntityOptional.get().setVolume(tickerOhlcEntity.getVolume());
//                                }
//                                tickerRepository.save(tickerOhlcEntityOptional.get());
                    }
                    else {
//                        LOGGER.info("\t\t\tSaving...{}", tickerOhlcEntity.toString());
                        dayCounter++;
                        candleDailyRepository.save(candleDailyEntity);
                    }

                }
            }

            if (dayCounter > 0) {
                LOGGER.info("\t\t\tSaved {} OHLCs", dayCounter);
            }

        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(csvReader);
        }
    }

}
