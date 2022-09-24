package com.rno.tickerscanner.crunch;

import com.rno.tickerscanner.Criteria;
import com.rno.tickerscanner.dao.IndicatorRepository;
import com.rno.tickerscanner.dao.TickRepository;
import com.rno.tickerscanner.dao.entity.IndicatorEntity;
import com.rno.tickerscanner.dao.entity.TickEntity;
import com.rno.tickerscanner.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@AllArgsConstructor
public class Cruncher {

  private final TickRepository tickRepository;
  private final IndicatorRepository indicatorRepository;

  public void crunch(Criteria criteria) {



    // check if latest OHLCV

    // crunch ind
    indicatorRepository.createIndTable(criteria.getTableName());

    Optional<IndicatorEntity> latestIndicator = indicatorRepository.findLatest(
      criteria,
        "TSLA"
    );

    System.out.println(latestIndicator);



    // check if latest

    // crunch it

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
