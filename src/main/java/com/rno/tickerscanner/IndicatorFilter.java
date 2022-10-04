package com.rno.tickerscanner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorFilter implements Filter {

  private IndicatorEnum indicator;
  private int range;
  private int offset;

  public String getTableName() {
    return "ind_" + indicator.name() + range + "_" + offset;
  }

}
