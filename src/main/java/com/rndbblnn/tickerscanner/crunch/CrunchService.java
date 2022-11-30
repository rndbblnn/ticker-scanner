package com.rndbblnn.tickerscanner.crunch;

import com.google.common.base.Stopwatch;
import com.rndbblnn.tickerscanner.aql.filter.Filter;
import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
import com.rndbblnn.tickerscanner.dao.CandleDailyRepository;
import com.rndbblnn.tickerscanner.dao.IndicatorRepository;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CrunchService {

  private final CandleDailyRepository candleDailyRepository;
  private final IndicatorRepository indicatorRepository;

  public void prepare() {
    // check if latest OHLCV

    // crunch ticks table (ATR)
    log.info("updating TR...");
    Stopwatch sw = Stopwatch.createStarted();
    candleDailyRepository.updateTr();
    log.info("updating TR [done: {}]", sw);

    log.info("updating TR % ...");
    sw.reset().start();
    candleDailyRepository.updateTrPct();
    log.info("updating TR % [done: {}]", sw);
  }

  @Async
//  @Cacheable(cacheNames = "CrunchService-crunch", key="#filter.toString()")
  public CompletableFuture<Void> crunch(Filter filter) {
    Stopwatch sw = Stopwatch.createStarted();
    if (filter instanceof IndicatorFilter) {
      IndicatorFilter indicatorFilter = (IndicatorFilter) filter;

      if (!indicatorFilter.getIndicator().isCrunchRequired()) {
        return CompletableFuture.completedFuture(null);
      }

      log.info("\tcrunching {}", indicatorFilter);

      indicatorRepository.createIndTable(indicatorFilter.getTableName());
      if (indicatorFilter.getIndicator().isAggFunction()) {
        indicatorRepository.crunchAggFunction(indicatorFilter);
      } else {
        switch (indicatorFilter.getIndicator()) {
          case DV:
            indicatorRepository.crunchDv(indicatorFilter);
            break;
          case AVGDV:
          case MINDV:
          case MAXDV:
            indicatorRepository.crunchDvAggFunction(indicatorFilter);
            break;
          case EMA:
            indicatorRepository.crunchEMA(indicatorFilter);
            break;
          case ATR:
            indicatorRepository.crunchATR(indicatorFilter);
            break;
        }
      }
    }
    log.info("\tcrunching done [elapsed: {}, filter:{}]", sw, filter);

    return CompletableFuture.completedFuture(null);
  }








}
