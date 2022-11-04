package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
import com.rndbblnn.tickerscanner.dao.entity.IndicatorEntity;
import java.util.Locale;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class IndicatorRepository {

  public static final String CREATE_TABLE_COLUMNS =
      "    id varchar(36) NOT NULL DEFAULT gen_random_uuid()," +
      "    symbol varchar(12) NOT NULL," +
      "    tick_time timestamp NOT NULL," +
      "    value DOUBLE PRECISION NULL," +
      "    CONSTRAINT %s_pk PRIMARY KEY (id)," +
      "    CONSTRAINT %s_uk UNIQUE (symbol, tick_time)";

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
  public void dropAllIndicatorTables() {
    namedParameterJdbcTemplate.query(
            "SELECT table_name FROM information_schema.tables \n" +
                "WHERE table_catalog = 'stonks'\n" +
                "AND table_name LIKE 'ind_%';",
            new MapSqlParameterSource(),
            (rs, rowNum) -> rs.getString("table_name")
        )
        .stream()
        .forEach(tableName -> {
          entityManager.createNativeQuery("DROP TABLE " + tableName).executeUpdate();
        });
  }

  @Transactional
  public void createIndTable(String tableName) {
    entityManager.createNativeQuery(
            "CREATE TABLE IF NOT EXISTS " + tableName +
                " (" + String.format(CREATE_TABLE_COLUMNS, tableName, tableName)  +")"
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
    this.insertSymbols(indicatorFilter.getTableName());

    this.crunchTicksColumn(
        indicatorFilter,
        toAggFunctionFull(
            toAggFunctionName(indicatorFilter),
            toOhlcvColumnName(Character.toString(indicatorFilter.getIndicator().name().charAt(3)))
        )
    );
  }

  public void crunchDv(IndicatorFilter indicatorFilter) {
    this.insertSymbols(indicatorFilter.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = ind.value \n" +
            "FROM (\n" +
            "\tSELECT symbol, tick_time, \n" +
            "\t\t((high_price + low_price)/2) * volume / 1000000 AS value\n" +
            "\tFROM candle_d\n" +
            ") ind\n" +
            " WHERE src.value IS NULL\n" +
            " AND src.symbol = ind.symbol\n" +
            " AND src.tick_time = ind.tick_time",
        new MapSqlParameterSource()
    );
  }

  public void crunchDvAggFunction(IndicatorFilter indicatorFilter) {
    this.insertSymbols(indicatorFilter.getTableName());

    this.crunchTicksColumn(indicatorFilter,
        toAggFunctionFull(
            toAggFunctionName(indicatorFilter),
            "((high_price + low_price)/2) * volume / 1000000")
    );
  }

  private void insertSymbols(String tableName) {
    namedParameterJdbcTemplate.update(
        "INSERT INTO " + tableName + " (id, symbol, tick_time, value)\n" +
            "\tSELECT gen_random_uuid(), candle_d.symbol, candle_d.tick_time, null \n" +
            "\tFROM candle_d\n" +
            "\tLEFT OUTER JOIN " + tableName + " ON candle_d.symbol = " + tableName + ".symbol\n" +
            "\t  AND candle_d.tick_time  = " + tableName + ".tick_time\n" +
            "\tWHERE " + tableName + ".symbol IS NULL \n" +
            "ON CONFLICT DO NOTHING",
        new MapSqlParameterSource()
    );
  }

  public void crunchATR(IndicatorFilter indicatorFilter) {
    this.insertSymbols(indicatorFilter.getTableName());
    this.crunchTicksColumn(indicatorFilter,
        toAggFunctionFull("AVG", "tr_pct")
    );
  }

  private void crunchTicksColumn(IndicatorFilter indicatorFilter, String aggFunctionFull) {
    namedParameterJdbcTemplate.update(
        "UPDATE " + indicatorFilter.getTableName() + " src SET value = tmp.value \n" +
            "FROM (\n" +
            "\t\tSELECT symbol, tick_time, " + aggFunctionFull + " OVER(ORDER BY symbol,tick_time ROWS BETWEEN :range PRECEDING AND CURRENT ROW) AS value\n" +
            "\t\tFROM candle_d\n" +
            ") tmp\n" +
            "  WHERE src.value IS NULL\n" +
            " AND src.symbol = tmp.symbol\n" +
            " AND src.tick_time = tmp.tick_time;",
        new MapSqlParameterSource()
            .addValue("range", indicatorFilter.getRange() - 1)
    );
  }

  private static final String toAggFunctionFull(String aggFunctionName, String aggFunctionParameter) {
    return aggFunctionName + "(" + aggFunctionParameter + ")";
  }

  private static final String toAggFunctionName(IndicatorFilter indicatorFilter) {
    switch (indicatorFilter.getIndicator()) {
      case AVGO:
      case AVGH:
      case AVGL:
      case AVGC:
      case AVGV:
      case MINO:
      case MINH:
      case MINL:
      case MINC:
      case MINV:
      case MAXO:
      case MAXH:
      case MAXL:
      case MAXC:
      case MAXV:
      case AVGDV:
      case MINDV:
      case MAXDV:
        break;
      default:
        throw new RuntimeException("Unsupported IndicatorFilter: " + indicatorFilter);
    }

    return indicatorFilter.getIndicator().name().substring(0, 3);
  }


  private static final String toOhlcvColumnName(String ohlcv) {
    switch (ohlcv.toUpperCase(Locale.ROOT)) {
      case "O":
        return "open_price";
      case "H":
        return "high_price";
      case "L":
        return "low_price";
      case "C":
        return "close_price";
      case "V":
        return "volume";
      default:
        throw new RuntimeException("Unsupported ohlcv: " + ohlcv);
    }
  }
}
