package com.rndbblnn.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class HistoricalChart {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime date;
    public double open;
    public double low;
    public double high;
    public double close;
    public long volume;

}
