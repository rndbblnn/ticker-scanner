package com.rno.tickerscanner.aql;

public enum OperatorEnum {

  EQUAL, GREATER, LESSER, GREATER_OR_EQUAL, LESSER_OR_EQUAL;

  public static final OperatorEnum fromLine(String line) {
    if (line.matches(".*>=.*")) {
      return GREATER_OR_EQUAL;
    }
    if (line.matches(".*<=.*")) {
      return LESSER_OR_EQUAL;
    }
    if (line.matches(".*<.*")) {
      return LESSER;
    }
    if (line.matches(".*>.*")) {
      return GREATER;
    }
    if (line.matches(".*=.*")) {
      return EQUAL;
    }
    throw new RuntimeException("no operator? [line: " + line + "]");
  }

  public String toSign() {
    switch (this) {
      case EQUAL:
        return "=";
      case LESSER:
        return "<";
      case LESSER_OR_EQUAL:
        return "<=";
      case GREATER:
        return ">";
      case GREATER_OR_EQUAL:
        return ">=";
    }
    throw new RuntimeException("Unsupported operator: " + this);

  }

}
