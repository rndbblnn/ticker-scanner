package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.IndicatorFilter;
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
                "    CONSTRAINT "+ tableName +"_pk PRIMARY KEY (id)," +
                "    CONSTRAINT "+ tableName +"_uk UNIQUE (symbol, tick_time)" +
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

  public void crunchSma(IndicatorFilter criteria) {
    this.insertSymbols(criteria.getTableName());
    namedParameterJdbcTemplate.update(
        "UPDATE "+ criteria.getTableName() +" src SET value = ind.value \n" +
            "FROM (\n" +
            "\tselect symbol, tick_time, \n" +
            "\t\tAVG(close_price) OVER(ORDER BY tick_time ROWS BETWEEN :range PRECEDING AND CURRENT ROW) AS value\n" +
            "\t  FROM ticks\n" +
            "\t ) ind\n" +
            " WHERE src.value IS NULL\n" +
            " AND src.symbol = ind.symbol\n" +
            " AND src.tick_time = ind.tick_time",
        new MapSqlParameterSource("range", criteria.getRange()-1)
    );
  }


  private void insertSymbols(String tableName) {
    namedParameterJdbcTemplate.update(
        "INSERT INTO "+ tableName +" (id, symbol, tick_time, value)\n" +
            "\tSELECT gen_random_uuid(), ticks.symbol, ticks.tick_time, null \n" +
            "\tFROM ticks\n" +
            "\tLEFT OUTER JOIN "+ tableName +" ON ticks.symbol = "+ tableName +".symbol\n" +
            "\t  AND ticks.tick_time  = "+ tableName +".tick_time\n" +
            "\tWHERE "+ tableName +".value IS NULL",
        new MapSqlParameterSource()
    );

  }

}
