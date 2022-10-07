package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.aql.Filter;
import com.rno.tickerscanner.aql.IndicatorFilter;
import com.rno.tickerscanner.aql.NumberFilter;
import com.rno.tickerscanner.aql.OperatorEnum;
import com.rno.tickerscanner.dto.PatternMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class QueryRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Async
  public CompletableFuture<List<PatternMatchDto>> findPatternMatches(
      Filter leftFilter,
      OperatorEnum operator,
      Filter rightFilter) throws InterruptedException {

    List<PatternMatchDto> results =
        namedParameterJdbcTemplate.query(
            "SELECT left1.symbol, left1.tick_time FROM (\n" +
                getFilterQueryStr(leftFilter) +
                ") left1\n" +
                "JOIN (\n" +
                getFilterQueryStr(rightFilter) +
                ") right1\n" +
                "USING (symbol, tick_time)\n" +
                "WHERE left1.lag_value " + operator.toSign() + " right1.lag_value",
            new MapSqlParameterSource(),
            (rs, rowNum) -> new PatternMatchDto()
                .setSymbol(rs.getString("symbol"))
                .setPatternTime(rs.getObject("tick_time", LocalDateTime.class))
        );

    return CompletableFuture.completedFuture(results);

  }

  private String getFilterQueryStr(Filter filter) {
    if (filter instanceof IndicatorFilter) {
      return "    SELECT symbol, tick_time, LAG(" + ((IndicatorFilter) filter).getColumnName() + ", 1) OVER(ORDER BY symbol, tick_time) AS lag_value\n" +
          "    FROM " + ((IndicatorFilter) filter).getTableName() + "\n";
    }
    if (filter instanceof NumberFilter) {
      return "    SELECT symbol, tick_time, " + ((NumberFilter) filter).getNumber() + " AS lag_value " +
          "    FROM ticks";
    }
    throw new RuntimeException("Unsupported filter: " + filter);
  }


}
