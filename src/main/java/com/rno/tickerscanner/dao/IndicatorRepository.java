package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.aql.IndicatorFilter;
import com.rno.tickerscanner.dao.entity.IndicatorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class IndicatorRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public boolean existsByTableName(String tableName) {
    return entityManager.createNativeQuery(
            "SELECT to_regclass('public." + tableName + "')) "
        )
        .getSingleResult() != null;
  }

  @Transactional
  public void createIndTable(String tableName) {
    entityManager.createNativeQuery(
            "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "    id varchar(36) NOT NULL," +
                "    symbol varchar(12) NOT NULL," +
                "    tick_time timestamp NOT NULL," +
                "    value DOUBLE PRECISION NULL," +
                "    CONSTRAINT " + tableName + "_pk PRIMARY KEY (id)," +
                "    CONSTRAINT " + tableName + "_uk UNIQUE (symbol, tick_time)" +
                ")"
        )
        .executeUpdate();
  }

  public Optional<IndicatorEntity> findLatest(IndicatorFilter criteria, String symbol) {
    return namedParameterJdbcTemplate.query(
        "SELECT id,symbol,tick_time FROM " + criteria.getTableName() +
            " WHERE symbol = :symbol " +
            "LIMIT 1 OFFSET :offset",
        new MapSqlParameterSource()
            .addValue("symbol", symbol)
            .addValue("offset", criteria.getRange() + criteria.getOffset()),
        (rs, rowNum) ->
            new IndicatorEntity()
                .setSymbol(rs.getString("symbol"))
    ).stream().findFirst();
  }

  public void crunchAggFunction(IndicatorFilter indicatorFilter) {
    String functionStr;
    switch (indicatorFilter.getIndicator()) {
      case AVGO:
      case AVGH:
      case AVGL:
      case AVGC:
      case AVGV:
        functionStr = "AVG";
        break;
      case MINO:
      case MINH:
      case MINL:
      case MINC:
      case MINV:
        functionStr = "MIN";
        break;
      case MAXO:
      case MAXH:
      case MAXL:
      case MAXC:
      case MAXV:
        functionStr = "MAX";
        break;
      default:
        throw new RuntimeException("Unsupported IndicatorFilter: " + indicatorFilter);
    }

    this.insertSymbols(indicatorFilter.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = tmp.value \n" +
            "FROM (\n" +
            "\tSELECT symbol, tick_time, LAG(value, :offset) OVER (ORDER BY symbol,tick_time) AS value \n" +
            "\tFROM (\n" +
            "\t\tSELECT symbol, tick_time, " + functionStr + "(close_price) OVER(ORDER BY tick_time ROWS BETWEEN :range PRECEDING AND CURRENT ROW) AS value\n" +
            "\t\tFROM ticks\n" +
            "\t ) ind\n" +
            ") tmp\n" +
            "  WHERE src.value IS NULL\n" +
            " AND src.symbol = tmp.symbol\n" +
            " AND src.tick_time = tmp.tick_time;",
        new MapSqlParameterSource()
            .addValue("range", indicatorFilter.getRange() - 1)
            .addValue("offset", indicatorFilter.getOffset())
    );
  }

  public void crunchDv(IndicatorFilter indicatorFilter) {
    this.insertSymbols(indicatorFilter.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = ind.value \n" +
            "FROM (\n" +
            "\tSELECT symbol, tick_time, \n" +
            "\t\tlag(((high_price + low_price)/2) * volume / 1000000, :offset) OVER(ORDER BY symbol,tick_time)\n" +
            "\t\t    AS value\n" +
            "\tFROM ticks\n" +
            ") ind\n" +
            " WHERE src.value IS NULL\n" +
            " AND src.symbol = ind.symbol\n" +
            " AND src.tick_time = ind.tick_time",
        new MapSqlParameterSource("offset", indicatorFilter.getOffset())
    );
  }

  public void crunchDvAggFunction(IndicatorFilter indicatorFilter) {
    String functionStr;
    switch (indicatorFilter.getIndicator()) {
      case AVGDV:
        functionStr = "AVG";
        break;
      case MINDV:
        functionStr = "MIN";
        break;
      case MAXDV:
        functionStr = "MAX";
        break;
      default:
        throw new RuntimeException("Unsupported IndicatorFilter: " + indicatorFilter);
    }

    this.insertSymbols(indicatorFilter.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = tmp.value \n" +
            "FROM (\n" +
            "\tSELECT symbol, tick_time, LAG(value, :offset) OVER (ORDER BY symbol,tick_time) AS value \n" +
            "\tFROM (\n" +
            "\t\tSELECT symbol, tick_time, " + functionStr + "(((high_price + low_price)/2) * volume / 1000000) OVER(ORDER BY tick_time ROWS BETWEEN :range PRECEDING AND CURRENT ROW) AS value\n" +
            "\t\tFROM ticks\n" +
            "\t ) ind\n" +
            ") tmp\n" +
            "  WHERE src.value IS NULL\n" +
            " AND src.symbol = tmp.symbol\n" +
            " AND src.tick_time = tmp.tick_time;",
        new MapSqlParameterSource()
            .addValue("range", indicatorFilter.getRange() - 1)
            .addValue("offset", indicatorFilter.getOffset())
    );
  }

  private void insertSymbols(String tableName) {
    namedParameterJdbcTemplate.update(
        "INSERT INTO " + tableName + " (id, symbol, tick_time, value)\n" +
            "\tSELECT gen_random_uuid(), ticks.symbol, ticks.tick_time, null \n" +
            "\tFROM ticks\n" +
            "\tLEFT OUTER JOIN " + tableName + " ON ticks.symbol = " + tableName + ".symbol\n" +
            "\t  AND ticks.tick_time  = " + tableName + ".tick_time\n" +
            "\tWHERE " + tableName + ".symbol IS NULL \n" +
            "ON CONFLICT DO NOTHING",
        new MapSqlParameterSource()
    );
  }

  public void crunchATR(IndicatorFilter indicatorFilter) {
    this.insertSymbols(indicatorFilter.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = tmp.value \n" +
            "FROM (\n" +
            "\tSELECT symbol, tick_time, LAG(value, :offset) OVER (ORDER BY symbol,tick_time) AS value \n" +
            "\tFROM (\n" +
            "\t\tSELECT symbol, tick_time, AVG(tr_pct) OVER(ORDER BY tick_time ROWS BETWEEN :range PRECEDING AND CURRENT ROW) AS value\n" +
            "\t\tFROM ticks\n" +
            "\t ) ind\n" +
            ") tmp\n" +
            "  WHERE src.value IS NULL\n" +
            " AND src.symbol = tmp.symbol\n" +
            " AND src.tick_time = tmp.tick_time;",
        new MapSqlParameterSource()
            .addValue("range", indicatorFilter.getRange() - 1)
            .addValue("offset", indicatorFilter.getOffset())
    );
  }
}
