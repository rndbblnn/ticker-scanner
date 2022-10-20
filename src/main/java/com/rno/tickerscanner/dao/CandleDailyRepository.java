package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.dao.entity.CandleDailyEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CandleDailyRepository extends CrudRepository<CandleDailyEntity, Long> {

  boolean existsTickEntityByTickTimeAndSymbol(LocalDateTime tickTime, String symbol);

  Optional<CandleDailyEntity> findBySymbolAndTickTime(String symbol, LocalDateTime tickTime);

  @Query(nativeQuery = true, value =
      "SELECT * FROM candle_d WHERE symbol = :symbol ORDER BY tick_time DESC LIMIT 1")
  Optional<CandleDailyEntity> findLatestByTickerName(String symbol);

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value =
      "UPDATE candle_d t SET tr = sub.tr\n" +
          "FROM (\n" +
          "  SELECT symbol, tick_time, GREATEST(high_price-low_price,high_price-prev_close,low_price-prev_close) AS tr\n" +
          "  FROM (\n" +
          "    SELECT *,\n" +
          "      CASE\n" +
          "          WHEN high_price > low_price THEN prev_close\n" +
          "          ELSE low_price\n" +
          "        END \n" +
          "        AS hl,   \n" +
          "      CASE\n" +
          "          WHEN high_price > prev_close THEN high_price\n" +
          "          ELSE prev_close\n" +
          "        END \n" +
          "        AS hc,\n" +
          "      CASE\n" +
          "          WHEN low_price > prev_close THEN prev_close\n" +
          "          ELSE low_price\n" +
          "        END \n" +
          "        AS lc    \n" +
          "    FROM (\n" +
          "      SELECT symbol, tick_time, open_price, high_price, low_price, close_price, prev_low, prev_close FROM (\n" +
          "        select symbol, tick_time,  open_price, high_price, low_price, close_price,\n" +
          "            lag(close_price, 1) OVER(ORDER BY symbol,tick_time)\n" +
          "                AS prev_close,\n" +
          "            lag(low_price, 1) OVER(ORDER BY symbol,tick_time)\n" +
          "                AS prev_low\n" +
          "        FROM candle_d\n" +
          "      ) subq111\n" +
          "    ) subq11\n" +
          "  ) subq1 \n" +
          ") sub\n" +
          "WHERE t.tr IS NULL \n" +
          "AND t.symbol = sub.symbol\n" +
          "AND t.tick_time  = sub.tick_time")
  void updateTr();

  @Transactional
  @Modifying
  @Query(nativeQuery = true, value =
      "UPDATE candle_d " +
          "SET tr_pct = (100*(tr/close_price)) " +
          "WHERE tr_pct IS NULL")
  void updateTrPct();

  @Query(nativeQuery = true,
      value =
          "SELECT * FROM candle_d "
              + "WHERE symbol = :symbol "
              + "AND tick_time >= :from "
              + "AND tick_time <= :to "
              + "ORDER BY tick_time"
  )
  List<CandleDailyEntity> findAll(String symbol, LocalDateTime from, LocalDateTime to);
}
