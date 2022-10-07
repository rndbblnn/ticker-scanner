package com.rno.tickerscanner.dao;

import com.rno.tickerscanner.BaseIntegrationTest;
import com.rno.tickerscanner.aql.IndicatorEnum;
import com.rno.tickerscanner.aql.IndicatorFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IndicatorRepositoryTest extends BaseIntegrationTest {

  @Autowired
  private IndicatorRepository indicatorRepository;

  @Test
  public void dropAllIndicatorTables() {
    indicatorRepository.dropAllIndicatorTables();
  }

  @Test
  public void crunchAggFunction() {

    IndicatorFilter indicatorFilter =
        new IndicatorFilter()
        .setIndicator(IndicatorEnum.AVGC)
        .setRange(10);

    indicatorRepository.dropAllIndicatorTables();
    indicatorRepository.createIndTable(indicatorFilter.getTableName());
    indicatorRepository.crunchAggFunction(indicatorFilter);
  }

}
