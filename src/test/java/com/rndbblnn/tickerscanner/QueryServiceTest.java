package com.rndbblnn.tickerscanner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryServiceTest extends BaseIntegrationTest {

  public static final String SCAN_TEST_QUERY =
      "[d]O.0 / [d]C.1 > 1.1 \n" +
          "AND [d]C.0 > [d]AVGC20.0 \n" +
          "AND [d]AVGC10.0 > [d]AVGC20.0 \n" +
          "AND [d]AVGC20.0 > [d]AVGC50.0 \n" +
          "AND [d]AVGC50.0 > [d]AVGC100.0 \n" +
          "AND [d]AVGC100.0 > [d]AVGC200.0 \n" +
          "AND [d]AVGC100.0 > [d]AVGC200.0 \n" +
          "AND (\n" +
          "   [d]DV.0 > 1.0 \n" +
          "   OR [d]MINDV3.1 > 2.0\n" +
          "   OR [d]AVGDV20.0 > 3.0\n" +
          ")\n" +
          "AND (\n" +
          "   [d]ATR1.0 > 8.0 \n" +
          "   OR [d]ATR20.0 > 5.0 \n" +
          "   OR [d]ATR20.20 > 5.0 \n" +
          "   OR [d]ATR20.40 > 5.0 \n" +
          ")\n";

  @Autowired
  private QueryService queryService;

  @Test
  @SneakyThrows
  public void scanTest() {

//    System.out.println(queryService.search("[d]C.0 / [d]C.1 > 1.1"));
    System.out.println(queryService.search("200([d]C.1 < [d]EMA200.1)"));
//    queryService.search("1.0 / 1.0 > 1.1");
//    System.out.println(queryService.search(SCAN_TEST_QUERY).size());

  }



}
