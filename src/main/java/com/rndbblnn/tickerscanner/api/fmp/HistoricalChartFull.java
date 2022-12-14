package com.rndbblnn.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class HistoricalChartFull {

    private String symbol;

    private List<Historical> historical = new ArrayList<>();

    @Data
    public static class Historical {
        @JsonFormat(pattern="yyyy-MM-dd")
        public LocalDate date;
        public double open;
        public double low;
        public double high;
        public double close;
        public long volume;
    }

}
