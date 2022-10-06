package com.rno.tickerscanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScannerServiceIntegrationTest extends BaseIntegrationTest {

  public static final String SCAN_TEST_QUERY =
      "[d]ATR20.0 > 5\n" +
          "AND [d]C.0 > [d]AVGC10.0 \n" +
          "AND [d]C.0 <= [d]AVGC20.0 \n" +
          "AND\n" +
          "(\n" +
          "   [d]DV.1 > 3 \n" +
          "   OR [d]MINDV5.5 > 3\n" +
          ")\n";

  @Autowired
  private ScannerService scannerService;

  @Test
  public void scanTest() {

    scannerService.scan(SCAN_TEST_QUERY);

  }

}
