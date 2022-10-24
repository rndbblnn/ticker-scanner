package com.rndbblnn.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SharesFloat {
    public String symbol;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public double freeFloat;
    public double floatShares;
    public double outstandingShares;
    public String source;
}
