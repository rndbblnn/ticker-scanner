package com.rno.tickerscanner.aql;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// @Builder
@Slf4j
public class Parser {

  private static final String OPERATOR_PATTERN_STR = "(\\>\\=|\\<\\=|\\=|\\<|\\>)";
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

    CriteriaGroup criteriaGroup = null;

    for (String line : lineArr) {

      line = line.replaceAll("\\s", "");

      // AND OR
      if (criteriaGroup != null && line.startsWith("AND")) {
        if (line.matches("^AND$")) {
          queryList.add(AndOrEnum.AND);
        }
        else {
          criteriaGroup.add(AndOrEnum.AND);
        }
      }
      else if (criteriaGroup != null && line.startsWith("OR")) {
        if (line.matches("^OR$")) {
          queryList.add(AndOrEnum.OR);
        }
        else {
          criteriaGroup.add(AndOrEnum.OR);
        }
      }
      line = line.replaceAll("(AND|OR)", "");
      if (line.isBlank()) {
        continue;
      }

      // ( ) 
      if (line.startsWith("(")) {
        criteriaGroup = new CriteriaGroup();
        queryList.add(criteriaGroup);
        continue;
      }
      else if (line.endsWith(")")) {
        criteriaGroup = null;
        continue;
      }

      Criteria criteria = parseLine(line);
      if (criteriaGroup == null) {
        criteriaGroup = new CriteriaGroup();
        queryList.add(criteriaGroup);
      }
      
      criteriaGroup.add(criteria);
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
            System.out.println("\tfilterStr: " + filterStr);
            if (filterStr.matches("[0-9]")) {
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
            return null;
          })
          .collect(Collectors.toList());

      Criteria criteria = new Criteria(filters.get(0), OperatorEnum.fromLine(line), filters.get(1));

      System.out.println(criteria);

      return criteria;
  }
}
