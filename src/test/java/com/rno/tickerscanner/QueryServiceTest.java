package com.rno.tickerscanner;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class QueryServiceTest extends BaseIntegrationTest {

  public static final String SCAN_TEST_SIMPLE_QUERY =
      "[d]C.0 / [d]C.1 > 1.1";

  public static final String SCAN_TEST_QUERY =
      "AND [d]C.0 > [d]AVGC10.0 \n" +
          "AND [d]C.0 > [d]AVGC20.0 \n" +
          "AND [d]AVGC10.0 > [d]AVGC20.0 \n" +
          "AND [d]AVGC20.0 > [d]AVGC50.0 \n" +
          "AND [d]AVGC50.0 > [d]AVGC100.0 \n" +
          "AND [d]AVGC100.0 > [d]AVGC200.0 \n" +
          "AND [d]AVGC100.0 > [d]AVGC200.0 \n" +
          "AND\n" +
          "(\n" +
          "   [d]DV.0 > 1 \n" +
          "   OR [d]MINDV3.1 > 2\n" +
          "   OR [d]AVGDV20.0 > 3\n" +
          ")\n" +
          "AND\n" +
          "(\n" +
          "   [d]ATR1.0 > 8 \n" +
          "   OR [d]ATR20.0 > 5 \n" +
          "   OR [d]ATR20.20 > 5 \n" +
          "   OR [d]ATR20.40 > 5 \n" +
          ")\n";

  @Autowired
  private QueryService queryService;

  @Test
  @SneakyThrows
  public void scanTest() {

    queryService.search(SCAN_TEST_SIMPLE_QUERY);
//    queryService.search(SCAN_TEST_QUERY);

  }

}
