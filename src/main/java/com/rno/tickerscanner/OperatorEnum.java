package com.rno.tickerscanner;

public enum OperatorEnum {

    EQUAL, GREATER, LESSER, GREATER_OR_EQUAL, LESSER_OR_EQUAL;

    public static final OperatorEnum fromLine(String line) {
        if (line.matches(".*<.*")) {
          return LESSER;
        }
        if (line.matches(".*>.*")) {
          return GREATER;
        }
        if (line.matches(".*>=.*")) {
          return GREATER_OR_EQUAL;
        }
        if (line.matches(".*<=.*")) {
          return LESSER_OR_EQUAL;
        }
        if (line.matches(".*=.*")) {
          return EQUAL;
        }
        throw new RuntimeException("no operator?");
      }

}
