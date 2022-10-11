package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.aql.AndOrEnum;
import com.rno.tickerscanner.aql.Criteria;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.aql.filter.ArithmeticFilter;
import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.aql.filter.IndicatorFilter;
import com.rno.tickerscanner.aql.filter.NumberFilter;
import com.rno.tickerscanner.dto.PatternMatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public void createTempTable(CriteriaGroup criteriaGroup) {
    entityManager.createNativeQuery(
        "CREATE UNLOGGED TABLE " + criteriaGroup.getTableName() +
            "(" + String.format(IndicatorRepository.CREATE_TABLE_COLUMNS, criteriaGroup.getTableName(), criteriaGroup.getTableName()) + ")"
    ).executeUpdate();
  }

  @Transactional
  public void insertCriteria(CriteriaGroup criteriaGroup, Criteria criteria) {
    namedParameterJdbcTemplate.update(
        "INSERT INTO " + criteriaGroup.getTableName() + "(symbol, tick_time) " +
            getCriteriaQueryStr(criteria) +
            " ON CONFLICT DO NOTHING",
        new MapSqlParameterSource()
    );
  }

  @Transactional
  public void deleteCriteria(CriteriaGroup criteriaGroup, Criteria criteria) {
    namedParameterJdbcTemplate.update(
        "DELETE FROM " + criteriaGroup.getTableName() + " tmp " +
            " WHERE NOT EXISTS (" + getCriteriaQueryStr(criteria) + " AND tmp.symbol = left1.symbol AND tmp.tick_time = left1.tick_time)",
        new MapSqlParameterSource()
    );
  }

  @Async
  public CompletableFuture<Integer> createTemporaryTable(Criteria criteria) throws InterruptedException {
    int updated =
        namedParameterJdbcTemplate.update(
            getCriteriaQueryStr(criteria),
            new MapSqlParameterSource()
        );

    return CompletableFuture.completedFuture(updated);

  }

  private String getCriteriaQueryStr(Criteria criteria) {
    return
        "SELECT left1.symbol, left1.tick_time FROM (\n" +
            getFilterQueryStr(criteria.getLeft()) +
            ") left1\n" +
            "JOIN (\n" +
            getFilterQueryStr(criteria.getRight()) +
            ") right1\n" +
            "USING (symbol, tick_time)\n" +
            "WHERE left1.lag_value " + criteria.getOperator().toSign() + " right1.lag_value";
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
    if (filter instanceof ArithmeticFilter) {

    }
    throw new RuntimeException("Unsupported filter: " + filter);
  }


  public List<PatternMatchDto> intersectAll(List<Object> queryList) {

    StringBuilder sb = new StringBuilder("SELECT * FROM " + ((CriteriaGroup) queryList.get(0)).getTableName());

    AndOrEnum andOr = null;
    for (int criteriaGroupIdx = 1; criteriaGroupIdx < queryList.size(); criteriaGroupIdx++) {
      Object critGroupObj = queryList.get(criteriaGroupIdx);
      if (critGroupObj instanceof AndOrEnum) {
        andOr = (AndOrEnum) critGroupObj;
        continue;
      }

      CriteriaGroup criteriaGroup = (CriteriaGroup) critGroupObj;

      if (andOr != null) {
        switch (andOr) {
          case AND:
            sb.append(" JOIN ");
            break;
          case OR:
            sb.append(" LEFT JOIN ");
            break;
        }
        sb.append(criteriaGroup.getTableName() + " USING (symbol, tick_time) ");
        andOr = null;
      }
    }

    sb.append(" ORDER BY tick_time, symbol");

    return namedParameterJdbcTemplate.query(sb.toString(), new MapSqlParameterSource(),
        (rs, rowNum) -> new PatternMatchDto()
            .setSymbol(rs.getString("symbol"))
            .setPatternTime(rs.getObject("tick_time", LocalDateTime.class))
    );
  }

}
