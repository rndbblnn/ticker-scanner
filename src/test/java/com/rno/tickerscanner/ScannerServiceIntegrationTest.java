package com.rno.tickerscanner;

import com.rno.tickerscanner.dao.IndicatorRepository;
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

  @Autowired
  private IndicatorRepository indicatorRepository;

  @Test
  public void scanTest() {

    scannerService.scan(SCAN_TEST_QUERY);

  }

  @Test
  public void dropAllIndicatorTables() {
    indicatorRepository.dropAllIndicatorTables();
  }

}
