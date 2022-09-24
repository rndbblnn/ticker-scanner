package com.rno.tickertracker.dao;

import com.rno.tickertracker.dao.entity.TickEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TickRepository extends CrudRepository<TickEntity, Long> {

    boolean existsTickEntityByTickTimeAndSymbol(LocalDateTime tickTime, String symbol);

    Optional<TickEntity> findTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);

    Iterable<TickEntity> findTop200BySymbolAndTickTimeLessThanEqualOrderByTickTimeDesc(String symbol, LocalDateTime tickTime);

    boolean existsTickEntityBySymbolAndTimeframeAndTickTime(String symbol, String timeframe, LocalDateTime tickTime);
}
