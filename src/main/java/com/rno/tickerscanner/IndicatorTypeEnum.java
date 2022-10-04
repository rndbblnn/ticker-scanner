package com.rno.tickerscanner;

import java.util.Arrays;

public enum IndicatorTypeEnum {

  SINGLE("O", "H", "L", "C", "V"),
  OPERATOR("AND", "OR"),
  AVG("ATR", "AVG", "DV");

  private String[] possibleValues;

  IndicatorTypeEnum(String... possibleValues) {
    this.possibleValues = possibleValues;
  }

  public static final IndicatorTypeEnum fromValue(String value) {
    for (IndicatorTypeEnum criteriaType : IndicatorTypeEnum.values()) {
      if (Arrays.stream(criteriaType.possibleValues).anyMatch(s -> s.matches(value))) {
        return criteriaType;
      }
    }
    throw new UnsupportedOperationException("Invalid IndicatorTypeEnum value: " + value);
  }

}
