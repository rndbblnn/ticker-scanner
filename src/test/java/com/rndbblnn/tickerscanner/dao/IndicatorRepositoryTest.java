package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.BaseIntegrationTest;
import com.rndbblnn.tickerscanner.aql.IndicatorEnum;
import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
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
