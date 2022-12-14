package com.rndbblnn.tickerscanner.api.fmp;

import com.rndbblnn.tickerscanner.BaseIntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FmpApiClientIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FmpApiClient fmpApiClient;

    @Test
    public void testGetHistoricalPriceFull() {

        HistoricalChartFull historicalChartFull =
                fmpApiClient.getHistoricalPriceFull(
                        "BCTX",
                        LocalDateTime.of(2020, 1, 1, 0, 0),
                        LocalDateTime.now());

        System.out.println(historicalChartFull);
    }

    @Test
    public void testGetHistoricalChart5Min() {

        List<HistoricalChart> historicalChartList =
                fmpApiClient.getHistoricalChart5Min(
                        "BCTX",
                        LocalDateTime.of(2020, 1, 1, 0, 0),
                        LocalDateTime.now());

        System.out.println(historicalChartList);


    }

}
