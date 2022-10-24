package com.rndbblnn.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MarketCap {

    public String symbol;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public long marketCap;
}
