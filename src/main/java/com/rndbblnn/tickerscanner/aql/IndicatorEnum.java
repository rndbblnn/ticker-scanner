package com.rndbblnn.tickerscanner.aql;

import java.util.Arrays;

public enum IndicatorEnum {

  AVGO, MINO, MAXO, AVGH, MINH, MAXH, AVGL, MINL, MAXL, AVGC, MINC, MAXC, AVGV, MINV, MAXV,
  AVGDV, MINDV, MAXDV, DV,
  ATR,
  O, H, L, C, V;

  public static final IndicatorEnum fromLine(String filterStr) {

    return Arrays.stream(IndicatorEnum.values())
        .filter(ind -> filterStr.matches(".*" + ind.name() + ".*"))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("no IndicatorEnum for: " + filterStr));

  }

  public boolean isAggFunction() {
    switch (this) {
      case AVGO:
      case AVGH:
      case AVGL:
      case AVGC:
      case AVGV:
      case MINO:
      case MINH:
      case MINL:
      case MINC:
      case MINV:
      case MAXO:
      case MAXH:
      case MAXL:
      case MAXC:
      case MAXV:
        return true;
    }
    return false;
  }

  public boolean isCrunchRequired() {
    switch (this) {
      case O:
      case H:
      case L:
      case C:
      case V:
        return false;
    }
    return true;
  }
}
