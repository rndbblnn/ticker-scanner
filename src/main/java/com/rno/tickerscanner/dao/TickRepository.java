package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.dao.entity.TickEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TickRepository extends CrudRepository<TickEntity, Long> {

    boolean existsTickEntityByTickTimeAndSymbol(LocalDateTime tickTime, String symbol);

    Optional<TickEntity> findTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);

    Iterable<TickEntity> findTop200BySymbolAndTickTimeLessThanEqualOrderByTickTimeDesc(String symbol, LocalDateTime tickTime);

    boolean existsTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);

    @Query(nativeQuery = true, value =
        "SELECT * FROM ticks WHERE symbol = :symbol ORDER BY tick_time DESC LIMIT 1")
    Optional<TickEntity> findLatestByTickerName(String symbol);
}
