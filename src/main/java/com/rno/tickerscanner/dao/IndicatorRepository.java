package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.Criteria;
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
                "    CONSTRAINT "+ tableName +"_pk PRIMARY KEY (id)," +
                "    CONSTRAINT "+ tableName +"_uk UNIQUE (symbol, tick_time)" +
                ")"
        )
        .executeUpdate();
  }

  public Optional<IndicatorEntity> findLatest(Criteria criteria, String symbol) {

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

//     return (IndicatorEntity) entityManager.createNativeQuery(
//        "SELECT * FROM :tableName" +
//            " WHERE symbol = :symbol  LIMIT 1 OFFSET :offset",
//         IndicatorEntity.class
//    )
//         .setParameter("tableName", criteria.getTableName())
//         .setParameter("symbol", symbol)
//         .setParameter("offset", criteria.getRange() + criteria.getOffset())
//         .getSingleResult();
  }

}
