package com.rndbblnn.tickerscanner.dao;

import com.rndbblnn.tickerscanner.aql.IndicatorEnum;
import com.rndbblnn.tickerscanner.aql.filter.ArithmeticFilter;
import com.rndbblnn.tickerscanner.aql.filter.ArithmeticOperator;
import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
import org.junit.jupiter.api.Test;

public class QueryRepositoryTest {

  @Test
  public void testGetFilterQueryStr_Arithmetic_gap() {

    String query =
        QueryRepository.getFilterQueryStr(
            new ArithmeticFilter()
                .setLeft(new IndicatorFilter()
                    .setIndicator(IndicatorEnum.C)
                    .setOffset(0))
                .setArithmeticOperator(ArithmeticOperator.DIVIDE)
                .setRight(new IndicatorFilter()
                    .setIndicator(IndicatorEnum.C)
                    .setOffset(1))
        );

    System.out.println(query);

  }

}
