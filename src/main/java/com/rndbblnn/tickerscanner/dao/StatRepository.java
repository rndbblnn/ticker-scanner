package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.dto.TopTradeDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StatRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<TopTradeDto> getTopShortsOfTheDay(LocalDate dateFrom, int limit) {

    LocalDate dateTo = dateFrom.plusDays(1);

    return namedParameterJdbcTemplate.query(
        "SELECT * FROM (\n"
            + "\tSELECT DISTINCT ON (symbol, high_price) tmp.symbol, tmp.maxh, candle_d.tick_time, candle_d.close_price, (candle_d.close_price/tmp.maxh)-1 AS pct FROM (\n"
            + "\t\tSELECT symbol, MAX(high_price) maxh FROM candle_1m \n"
            + "\t\tWHERE tick_time > :dateFrom \n"
            + "\t\tAND  tick_time < :dateTo \n"
            + "\t\tGROUP BY symbol \n"
            + "\t) tmp\n"
            + "\tLEFT JOIN candle_d ON tmp.symbol = candle_d.symbol\n"
            + "\t\tAND candle_d.tick_time = :dateFrom\n"
            + "\tWHERE candle_d.open_price > 3\n"
            + "\tAND candle_d.volume * candle_d.close_price > 2000000\n"
            + ") x\n"
            + "ORDER BY pct ASC\n"
            + "LIMIT :limit",
        new MapSqlParameterSource()
            .addValue("dateFrom", dateFrom)
            .addValue("dateTo", dateTo)
            .addValue("limit", limit),
        (rs, rowNum) -> new TopTradeDto()
            .setTopTickPrice(rs.getDouble("maxh"))
            .setPct(rs.getDouble("pct"))
            .setSymbol(rs.getString("symbol"))
            .setTickTime(rs.getObject("tick_time", LocalDateTime.class))
            .setClose(rs.getDouble("close_price"))

    );
  }


  public List<TopTradeDto> getTopLongsOfTheDay(LocalDate dateFrom, int limit) {

    LocalDate dateTo = dateFrom.plusDays(1);

    return namedParameterJdbcTemplate.query(
        "SELECT * FROM (\n"
            + "\tSELECT DISTINCT ON (symbol, low_price) tmp.symbol, tmp.minl, candle_d.tick_time, candle_d.close_price, (candle_d.close_price/tmp.minl)-1 AS pct FROM (\n"
            + "\t\tSELECT symbol, MIN(low_price) minl FROM candle_1m \n"
            + "\t\tWHERE tick_time > :dateFrom \n"
            + "\t\tAND  tick_time < :dateTo \n"
            + "\t\tGROUP BY symbol \n"
            + "\t) tmp\n"
            + "\tLEFT JOIN candle_d ON tmp.symbol = candle_d.symbol\n"
            + "\t\tAND candle_d.tick_time = :dateFrom\n"
            + "\tWHERE candle_d.open_price > 3\n"
            + "\tAND candle_d.volume * candle_d.close_price > 5000000\n"
            + ") x\n"
            + "ORDER BY pct DESC\n"
            + "LIMIT :limit",
        new MapSqlParameterSource()
            .addValue("dateFrom", dateFrom)
            .addValue("dateTo", dateTo)
            .addValue("limit", limit),
        (rs, rowNum) -> new TopTradeDto()
            .setTopTickPrice(rs.getDouble("minl"))
            .setPct(rs.getDouble("pct"))
            .setSymbol(rs.getString("symbol"))
            .setTickTime(rs.getObject("tick_time", LocalDateTime.class))
            .setClose(rs.getDouble("close_price"))

    );

  }
}
