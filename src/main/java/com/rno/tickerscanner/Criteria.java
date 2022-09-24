package com.rno.tickerscanner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Criteria {

  private IndicatorEnum indicator;
  private int range;
  private int offset;

  public String getTableName() {
    return "ind_" + indicator.name() + range + "_" + offset;
  }

}
