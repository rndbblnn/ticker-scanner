package com.rno.tickerscanner.crunch;

import com.rno.tickerscanner.IndicatorFilter;
import com.rno.tickerscanner.dao.IndicatorRepository;
import com.rno.tickerscanner.dao.TickRepository;
import com.rno.tickerscanner.dao.entity.TickEntity;
import com.rno.tickerscanner.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class Scan {

  private final TickRepository tickRepository;
  private final IndicatorRepository indicatorRepository;

  public void run(String query) {

    prepare();

    List<IndicatorFilter> criteriaList = this.parseQuery(query);
    criteriaList.forEach(this::crunchCriteria);

  }

  List<IndicatorFilter> parseQuery(String query) {
    List<IndicatorFilter> criteriaList = new ArrayList<>();



    return criteriaList;
  }

  private void prepare() {
    // check if latest OHLCV

    // crunch ticks table (TR)
    tickRepository.updateTr();
    tickRepository.updateTrPct();
  }

  private void crunchCriteria(IndicatorFilter criteria) {

    indicatorRepository.createIndTable(criteria.getTableName());

    switch (criteria.getIndicator()) {
      case AVGC:
        indicatorRepository.crunchSma(criteria);
        break;
    }
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
