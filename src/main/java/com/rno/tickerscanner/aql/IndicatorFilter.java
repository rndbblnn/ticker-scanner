package com.rno.tickerscanner.aql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorFilter implements Filter {

  private TimeframeEnum timeframe = TimeframeEnum.tf_d;
  private IndicatorEnum indicator;
  private int range;
  private int offset;

  public String getTableName() {
    return "ind_" + timeframe.toTimeframeStr() + "_" + indicator.name().toLowerCase(Locale.ROOT) + range;
  }

}
