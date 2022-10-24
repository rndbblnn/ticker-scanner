package com.rno.tickerscanner.web;

import com.rndbblnn.stonks.commons.entity.BaseCandle;
import com.rno.tickerscanner.aql.TimeframeEnum;
import com.rno.tickerscanner.dao.Candle1mRepository;
import com.rno.tickerscanner.dao.CandleDailyRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class ChartController {

  private final CandleDailyRepository candleDailyRepository;
  private final Candle1mRepository candle1mRepository;

  @GetMapping("/chart/{symbol}/{timeframe}")
  @SneakyThrows
  public ResponseEntity<APIResponse<List<? extends BaseCandle>>> getChart(
      @PathVariable String symbol,
      @PathVariable String timeframe,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate from,
      @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate to) {

    TimeframeEnum timeframeEnum = TimeframeEnum.fromTimeframeStr(timeframe);
    if (timeframeEnum == null) {
      log.error("Invalid timeframe: {}", timeframe);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    List<? extends BaseCandle> candleList;

    switch (timeframeEnum) {
      case tf_d:
        candleList = candleDailyRepository.findAll(symbol, from.atTime(0,0), to.atTime(23,59));
        break;
      case tf_1m:
        candleList = candleDailyRepository.findAll(symbol, from.atTime(0,0), to.atTime(23,59));
        break;
      default:
        log.error("Unsupported timeframe: {}", timeframeEnum);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    return ResponseEntity.ok(
        APIResponse.<List<? extends BaseCandle>>builder()
            .payload(candleList)
            .build()
    );

  }
}
