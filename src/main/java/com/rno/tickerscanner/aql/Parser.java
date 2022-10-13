package com.rno.tickerscanner.aql;

import com.rno.tickerscanner.aql.filter.ArithmeticFilter;
import com.rno.tickerscanner.aql.filter.ArithmeticOperator;
import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.aql.filter.IndicatorFilter;
import com.rno.tickerscanner.aql.filter.NumberFilter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// @Builder
@Slf4j
public class Parser {

  private static final String OPERATOR_PATTERN_STR = "(\\>\\=|\\<\\=|\\=|\\<|\\>)";
  private static final String ARITHMETIC_OPERATOR_PATTERN_STR = "(\\*|\\/|\\*-\\*+)";
  private static final String TIMEFRAME_PATTERN_STR = "(\\[(3m|5m|15m|30m|1h|4h|d|m)\\])";
  private static final String OHLCV_PATTERN_STR = TIMEFRAME_PATTERN_STR + "[O,H,L,C,V]\\.[0-9]{1,4}+$";
  private static final String DV_PATTERN_STR = TIMEFRAME_PATTERN_STR + "DV\\.[0-9]{1,4}+$";
  private static final String FUNCTION_PATTERN_STR = TIMEFRAME_PATTERN_STR + "[A-Z]{2,6}[0-9]{1,4}\\.[0-9]{1,4}";

  private String query;

  private static Parser _instance;
  public static Parser builder() {
    _instance = new Parser();
    return _instance;
  }

  public Parser query(String queryStr) {
    _instance.query = queryStr;
    return _instance;
  }

  public List<Object> build() {
    List<Object> queryList = new ArrayList<>();
    var lineArr = _instance.query.split("\\R");

    CriteriaGroup rootGroup = new CriteriaGroup();
    queryList.add(rootGroup);

    CriteriaGroup criteriaGroup = null;

    for (String line : lineArr) {

      System.err.println(line);
      line = line.replaceAll("\\s", "");

      CriteriaGroup criteriaGroupToAddTo = criteriaGroup != null ? criteriaGroup : rootGroup;

      // AND
      if (line.matches("^AND\\($")) {
        queryList.add(AndOrEnum.AND);
        criteriaGroup = new CriteriaGroup();
        queryList.add(criteriaGroup);
        continue;
      }
      else if (line.startsWith("AND")) {
        criteriaGroupToAddTo.add(AndOrEnum.AND);
        criteriaGroupToAddTo.add(parseLine(line.replaceAll("^AND","")));
        continue;
      }

      // OR
      if (line.matches("^OR\\($")) {
        queryList.add(AndOrEnum.OR);
        criteriaGroup = new CriteriaGroup();
        queryList.add(criteriaGroup);
        continue;
      }
      else if (line.startsWith("OR")) {
        criteriaGroupToAddTo.add(AndOrEnum.OR);
        criteriaGroup.add(parseLine(line.replaceAll("^OR","")));
        continue;
      }

      // ( ) 
      if (line.endsWith(")")) {
        criteriaGroup = null;
        continue;
      }

      criteriaGroupToAddTo.add(parseLine(line));

    }

    System.out.println("--");
    queryList.forEach(q -> {
      if (q instanceof CriteriaGroup) {
        
        System.out.println("\t" + Integer.toHexString(q.hashCode()));

        ((CriteriaGroup) q).getCriterias().forEach(c -> {
          System.out.println("\t\t" + c);
        });
      }
      else {
        System.out.println(q);
      }
    });

    return queryList;
  }

  private static Criteria parseLine(String line) {
    
    line = line.replaceAll("\\s", "");
    System.out.println("line: " + line);

    List<Filter> filters =
      Arrays.stream(line.split(OPERATOR_PATTERN_STR))
          .map(filterStr -> {
            System.out.println("\t\tfilterStr: " + filterStr);
            if (filterStr.matches(".*" + ARITHMETIC_OPERATOR_PATTERN_STR + ".*")) {
              Optional<ArithmeticOperator> arithmeticOperator = ArithmeticOperator.findOperator(filterStr);
              String leftFilterStr = filterStr.substring(0, filterStr.indexOf(arithmeticOperator.get().getSign()));
              String rightFilterStr = filterStr.substring(filterStr.indexOf(arithmeticOperator.get().getSign())+1);
              return new ArithmeticFilter()
                  .setLeft(getFilter(leftFilterStr))
                  .setArithmeticOperator(arithmeticOperator.get())
                  .setRight(getFilter(rightFilterStr));
            }
            else {
              return getFilter(filterStr);
            }
          })
          .collect(Collectors.toList());

      Criteria criteria = new Criteria()
          .setLeft(filters.get(0))
          .setOperator(OperatorEnum.fromLine(line))
          .setRight(filters.get(1));

      System.out.println(criteria);

      return criteria;
  }

  private static Filter getFilter(String filterStr) {
    if (filterStr.matches("(\\d+)?\\.(\\d+)?")) {
      return new NumberFilter(Double.valueOf(filterStr));
    }
    if (filterStr.matches(OHLCV_PATTERN_STR)) {
      return new IndicatorFilter()
          .setIndicator(IndicatorEnum.valueOf(Character.toString(filterStr.charAt(filterStr.indexOf("]") + 1))))
          .setOffset(Integer.valueOf(filterStr.substring(filterStr.indexOf(".")+1, filterStr.length())));
    }
    if (filterStr.matches(FUNCTION_PATTERN_STR)) {
      return new IndicatorFilter()
          .setIndicator(IndicatorEnum.fromLine(filterStr))
          .setOffset(Integer.valueOf(filterStr.substring(filterStr.indexOf(".")+1, filterStr.length())))
          .setRange(Integer.valueOf(filterStr.substring(0, filterStr.indexOf(".")).replaceAll("[^0-9{1,4}]", "")));
    }
    if (filterStr.matches(DV_PATTERN_STR)) {
      return new IndicatorFilter()
          .setIndicator(IndicatorEnum.fromLine(filterStr))
          .setOffset(Integer.valueOf(filterStr.substring(filterStr.indexOf(".")+1, filterStr.length())));
    }

    throw new RuntimeException("Unmatched filter. filterStr: " + filterStr);

  }
}
