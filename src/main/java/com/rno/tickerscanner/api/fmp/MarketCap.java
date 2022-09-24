package com.rno.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MarketCap {

    public String symbol;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public long marketCap;
}
