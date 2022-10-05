package com.rno.tickerscanner;

import java.util.Arrays;

public enum IndicatorEnum {
  
  AVGO, AVGH, AVGL, AVGC, AVGV,
  ATR,
  DV,
  O,H,L,C,V;

  public static final IndicatorEnum fromLine(String filterStr) {

    return Arrays.stream(IndicatorEnum.values())
        .filter(ind -> filterStr.matches(".*" + ind.name() + ".*"))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("no IndicatorEnum for: " + filterStr));

  }


}
