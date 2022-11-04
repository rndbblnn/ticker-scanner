package com.rndbblnn.tickerscanner.dto;

import com.rndbblnn.stonks.commons.entity.BaseCandle;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TopTradeDto extends BaseCandle {

  private String symbol;

  private LocalDateTime tickTime;

  private Double topTickPrice;

  private Double close;

  private Double pct;

}
