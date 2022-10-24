package com.rndbblnn.tickerscanner.aql.filter;

import com.rndbblnn.tickerscanner.aql.IndicatorEnum;
import com.rndbblnn.tickerscanner.aql.TimeframeEnum;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IndicatorFilter implements Filter {

  private TimeframeEnum timeframe = TimeframeEnum.tf_d;
  private IndicatorEnum indicator;
  private int range;
  @EqualsAndHashCode.Exclude
  private int offset;

  public String getTableName() {
    switch (indicator) {
      case O:
      case H:
      case L:
      case C:
      case V:
//      case DV:
        return "candle_" + timeframe.toTimeframeStr();
      default:
        return "ind_" + timeframe.toTimeframeStr() + "_" + indicator.name().toLowerCase(Locale.ROOT) + range;
    }
  }

  public String getColumnName() {
    switch (indicator) {
      case O:
        return "open_price";
      case H:
        return "high_price";
      case L:
        return "low_price";
      case C:
        return "close_price";
      case V:
        return "volume";
      default:
        return "value";
    }
  }
}
