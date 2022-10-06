package com.rno.tickerscanner.crunch;

import com.google.common.base.Stopwatch;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.aql.Filter;
import com.rno.tickerscanner.aql.IndicatorFilter;
import com.rno.tickerscanner.dao.IndicatorRepository;
import com.rno.tickerscanner.dao.TickRepository;
import com.rno.tickerscanner.dao.entity.TickEntity;
import com.rno.tickerscanner.dto.PatternMatchDto;
import com.rno.tickerscanner.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class CrunchService {

  private final TickRepository tickRepository;
  private final IndicatorRepository indicatorRepository;

  public void prepare() {
    // check if latest OHLCV

    // crunch ticks table (ATR)
    log.info("updating TR...");
    Stopwatch sw = Stopwatch.createStarted();
    tickRepository.updateTr();
    log.info("updating TR [done: {}]", sw);

    log.info("updating TR % ...");
    sw.reset().start();
    tickRepository.updateTrPct();
    log.info("updating TR % [done: {}]", sw);
  }

  public void crunchAll(Set<Filter> filterSet) {

    log.info("crunchAll enter [filterSet.size: {}]", filterSet.size());

    filterSet.stream()
        .forEach(filter -> {
          Stopwatch sw = Stopwatch.createStarted();
          if (filter instanceof IndicatorFilter) {
            IndicatorFilter indicatorFilter = (IndicatorFilter) filter;

            if (!indicatorFilter.getIndicator().isCrunchRequired()) {
              return;
            }

            log.info("" +
                "\tcrunching {}", indicatorFilter);

            indicatorRepository.createIndTable(indicatorFilter.getTableName());
            if (indicatorFilter.getIndicator().isAggFunction()) {
              indicatorRepository.crunchAggFunction(indicatorFilter);
            }

            switch (indicatorFilter.getIndicator()) {
              case DV:
                indicatorRepository.crunchDv(indicatorFilter);
                break;
              case AVGDV:
              case MINDV:
              case MAXDV:
                indicatorRepository.crunchDvAggFunction(indicatorFilter);
                break;
              case ATR:
                indicatorRepository.crunchATR(indicatorFilter);
                break;
            }

          }
          log.info("\tcrunching done [elapsed: {}, filter:{}]", sw, filter);
        });

  }

  public List<PatternMatchDto> findPatternMatches(CriteriaGroup criteriaGroup) {

    criteriaGroup.getCriterias()
        .stream()
        .map(criteria -> {

          return null;

        });

    return null;

  }

  public List<PatternMatchDto> findPatternMatches(Filter filter) {
    return null;
  }


  public void fetchOHLCV() {

    final LocalDate latestMarketDay = DateUtils.minusMarketDays(LocalDate.now(), 1);

    // get latest SPY
    Optional<TickEntity> tickEntityOpt = tickRepository.findLatestByTickerName("SPY");

    do {

      // call API

    } while (tickEntityOpt.get().getTickTime().isBefore(latestMarketDay.atTime(16, 0)));

    tickRepository.existsTickEntityByTickTimeAndSymbol(
        DateUtils.minusMarketDays(LocalDate.now(), 1).atTime(16, 0),
        "SPY"
    );

  }


}
