package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.aql.AndOrEnum;
import com.rndbblnn.tickerscanner.aql.Criteria;
import com.rndbblnn.tickerscanner.aql.CriteriaGroup;
import com.rndbblnn.tickerscanner.aql.filter.ArithmeticFilter;
import com.rndbblnn.tickerscanner.aql.filter.Filter;
import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
import com.rndbblnn.tickerscanner.aql.filter.NumberFilter;
import com.rndbblnn.tickerscanner.dto.PatternMatchDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class QueryRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public boolean existsUnloggedTable(String unloggedTableName) {
    return namedParameterJdbcTemplate.query(
            "SELECT 1 FROM pg_class WHERE relpersistence='u' AND lower(relname) = :unloggedTableName",
            new MapSqlParameterSource()
                .addValue("unloggedTableName", unloggedTableName.toLowerCase(Locale.ROOT)),
            (rs, rowNum) -> 1).stream()
        .findAny().isPresent();
  }

  @Transactional
  public void createTempTable(CriteriaGroup criteriaGroup) {
    entityManager.createNativeQuery(
        "CREATE UNLOGGED TABLE " + criteriaGroup.getTableName() +
            " (" + String.format(IndicatorRepository.CREATE_TABLE_COLUMNS, criteriaGroup.getTableName(), criteriaGroup.getTableName()) + ")"
    ).executeUpdate();
  }

  @Transactional
  public void insertCriteria(CriteriaGroup criteriaGroup, Criteria criteria) {
    String criteriaQueryStr = getCriteriaQueryStr(criteria);
    namedParameterJdbcTemplate.update(
        "INSERT INTO " + criteriaGroup.getTableName() + "(symbol, tick_time) \n" +
            criteriaQueryStr +
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

  public static final String getCriteriaQueryStr(Criteria criteria) {
    return
        "SELECT left1.symbol, left1.tick_time FROM (\n" +
            getFilterQueryStr(criteria.getLeft()) +
            ") left1\n" +
            "JOIN (\n" +
            getFilterQueryStr(criteria.getRight()) +
            ") right1 USING (symbol, tick_time)\n" +
            "WHERE left1.lag_value " + criteria.getOperator().toSign() + " right1.lag_value";
  }

  public static final String getFilterQueryStr(Filter filter) {
    if (filter instanceof IndicatorFilter) {
      return "    SELECT symbol, tick_time, LAG(" + ((IndicatorFilter) filter).getColumnName() + ", "
          + ((IndicatorFilter) filter).getOffset() + ") OVER(ORDER BY symbol, tick_time) AS lag_value\n" +
          "    FROM " + ((IndicatorFilter) filter).getTableName() + "\n";
    }
    if (filter instanceof NumberFilter) {
      return "    SELECT symbol, tick_time, " + ((NumberFilter) filter).getNumber() + " AS lag_value \n" +
          "    FROM candle_d\n";
    }
    if (filter instanceof ArithmeticFilter) {

      ArithmeticFilter arithmeticFilter = (ArithmeticFilter) filter;
      if (arithmeticFilter.getLeft() instanceof ArithmeticFilter || arithmeticFilter.getLeft() instanceof ArithmeticFilter) {
        throw new RuntimeException("Unsupported. ArithmeticFilter within ArithmeticFilter");
      }
      return
          "SELECT symbol, tick_time, (i1.lag_value " + arithmeticFilter.getArithmeticOperator().getSign() + " i2.lag_value) AS lag_value \n"
              +
              " FROM (\n" +
              "    " + getFilterQueryStr(arithmeticFilter.getLeft()) +
              ") i1 \n" +
              "JOIN (\n" +
              "    " + getFilterQueryStr(arithmeticFilter.getRight()) +
              ") i2 USING (symbol, tick_time) \n";

    }
    throw new RuntimeException("Unsupported filter: " + filter);
  }

  public List<PatternMatchDto> intersectAll(List<Object> queryList) {

    StringBuilder sb = new StringBuilder("SELECT * FROM " + ((CriteriaGroup) queryList.get(0)).getTableName() + "\n");

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
        sb.append(criteriaGroup.getTableName() + " USING (symbol, tick_time) \n");
        andOr = null;
      }
    }

    sb.append(" ORDER BY tick_time DESC, symbol");

    return namedParameterJdbcTemplate.query(sb.toString(), new MapSqlParameterSource(),
        (rs, rowNum) -> new PatternMatchDto()
            .setSymbol(rs.getString("symbol"))
            .setPatternTime(rs.getObject("tick_time", LocalDateTime.class))
    );
  }

  @Transactional
  public void dropAllTempTables() {
    namedParameterJdbcTemplate.query(
            "SELECT relname FROM pg_class WHERE relpersistence='u' AND relam = 2;",
            new MapSqlParameterSource(),
            (rs, rowNum) -> rs.getString("relname")
        )
        .stream()
        .forEach(tableName -> {
          entityManager.createNativeQuery("DROP TABLE " + tableName).executeUpdate();
        });
  }
}
