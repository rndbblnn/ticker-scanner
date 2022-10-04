package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.dao.entity.TickEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TickRepository extends CrudRepository<TickEntity, Long> {

  boolean existsTickEntityByTickTimeAndSymbol(LocalDateTime tickTime, String symbol);

  Optional<TickEntity> findTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);

  Iterable<TickEntity> findTop200BySymbolAndTickTimeLessThanEqualOrderByTickTimeDesc(String symbol, LocalDateTime tickTime);

  boolean existsTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);

  @Query(nativeQuery = true, value =
      "SELECT * FROM ticks WHERE symbol = :symbol ORDER BY tick_time DESC LIMIT 1")
  Optional<TickEntity> findLatestByTickerName(String symbol);

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value =
      "UPDATE ticks t SET tr = sub.tr\n" +
          "FROM (\n" +
          "\tSELECT symbol, tick_time, GREATEST(high_price-low_price,high_price-prev_close,low_price-prev_close) AS tr\n" +
          "\tFROM (\n" +
          "\t\tSELECT *,\n" +
          "\t\t\tCASE\n" +
          "\t\t\t    WHEN high_price > low_price THEN prev_close\n" +
          "\t\t\t    ELSE low_price\n" +
          "\t\t\t  END \n" +
          "\t\t\t  AS hl,\t \n" +
          "\t\t\tCASE\n" +
          "\t\t\t    WHEN high_price > prev_close THEN high_price\n" +
          "\t\t\t    ELSE prev_close\n" +
          "\t\t\t  END \n" +
          "\t\t\t  AS hc,\n" +
          "\t\t\tCASE\n" +
          "\t\t\t    WHEN low_price > prev_close THEN prev_close\n" +
          "\t\t\t    ELSE low_price\n" +
          "\t\t\t  END \n" +
          "\t\t\t  AS lc\t  \n" +
          "\t\tFROM (\n" +
          "\t\t\tSELECT symbol, tick_time, open_price, high_price, low_price, close_price, prev_low, prev_close FROM (\n" +
          "\t\t\t\tselect symbol, tick_time,  open_price, high_price, low_price, close_price,\n" +
          "\t\t\t\t\t\tlag(close_price, 1) OVER(ORDER BY symbol,tick_time\n" +
          "\t\t\t\t\t\t      ROWS BETWEEN 1 PRECEDING AND CURRENT ROW)\n" +
          "\t\t\t\t\t\t    AS prev_close,\n" +
          "\t\t\t\t\t\tlag(low_price, 1) OVER(ORDER BY symbol,tick_time\n" +
          "\t\t\t\t\t\t      ROWS BETWEEN 1 PRECEDING AND CURRENT ROW)\n" +
          "\t\t\t\t\t\t    AS prev_low\n" +
          "\t\t\t\tFROM ticks\n" +
          "\t\t\t) subq111\n" +
          "\t\t) subq11\n" +
          "\t) subq1 \n" +
          ") sub\n" +
          "WHERE t.tr IS NULL \n" +
          "AND t.symbol = sub.symbol\n" +
          "AND t.tick_time  = sub.tick_time")
  void updateTr();

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value =
      "UPDATE ticks " +
          "SET tr_pct = (100*(tr/close_price)) " +
          "WHERE tr_pct IS NULL")
  void updateTrPct();
}
