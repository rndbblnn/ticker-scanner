package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.stonks.commons.entity.Candle1mEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface Candle1mRepository extends CrudRepository<Candle1mEntity, Long> {

  @Query(nativeQuery = true,
      value =
          "SELECT * FROM candle_1m "
              + "WHERE symbol = :symbol "
              + "AND tick_time >= :from "
              + "AND tick_time <= :to "
              + "ORDER BY tick_time"
  )
  List<Candle1mEntity> findAll(String symbol, LocalDateTime from, LocalDateTime to);
}
