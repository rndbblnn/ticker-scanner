package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.stonks.commons.entity.CandleWeeklyEntity;
import org.springframework.data.repository.CrudRepository;

public interface CandleWeeklyRepository extends CrudRepository<CandleWeeklyEntity, Long> {

}
