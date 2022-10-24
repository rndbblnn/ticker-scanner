package com.rndbblnn.tickerscanner.aql.filter;

import lombok.Data;

@Data
public class ArithmeticFilter implements Filter {

  private Filter left;
  private ArithmeticOperator arithmeticOperator;
  private Filter right;

}
