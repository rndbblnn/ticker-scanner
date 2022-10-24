package com.rndbblnn.tickerscanner.aql.filter;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

public enum ArithmeticOperator {

  PLUS("+"),
  MINUS("-"),
  MULTIPLY("*"),
  DIVIDE("/");

  @Getter
  private String sign;

  ArithmeticOperator(String sign) {
    this.sign = sign;
  }

  public static Optional<ArithmeticOperator> findOperator(String string) {
    return
        Arrays.stream(ArithmeticOperator.values())
            .filter(operator -> string.contains(operator.getSign()))
            .findFirst();
  }
}
