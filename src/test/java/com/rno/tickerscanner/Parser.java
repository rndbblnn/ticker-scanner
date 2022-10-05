package com.rno.tickerscanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Parser {

  /**
   *
   */
  private static final String OPERATOR_PATTERN_STR = "(\\>\\=|\\<\\=|\\=|\\<|\\>)";
  private static final String TIMEFRAME_PATTERN_STR = "(\\[(3m|5m|15m|30m|1h|4h|d|m)\\])";
  private static final String INDICATOR_PATTERN_STR = TIMEFRAME_PATTERN_STR + "[O,H,L,C,V]{1}\\.[0-9]+$";
  private static final String FUNCTION_PATTERN_STR = TIMEFRAME_PATTERN_STR + "[A-Z]{3,6}[0-9]{1,4}\\.[0-9]{1,4}";

  private String query;
  private List<CriteriaGroup> criteriaList = new ArrayList<>();

  public List<CriteriaGroup> build(String query) {
    this.query = query.replaceAll("\\s", "");

    var lineArr = query.split("\\R");

    CriteriaGroup criteriaGroup = new CriteriaGroup();

    for (String line : lineArr) {
      if (line.startsWith("(")) {
        criteriaGroup.getCriterias().add(parseLine(line));
      }
    }

    return criteriaList;
  }

  private static Criteria parseLine(String line) {


    System.out.println("[d]SMA10.2".matches(FUNCTION_PATTERN_STR));

    
    line = line.replaceAll("\\s", "");
    System.out.println("line: " + line);



    List<Filter> filters = 
      Arrays.stream(line.split(OPERATOR_PATTERN_STR))
          .map(filterStr -> {
            System.out.println("\tfilterStr: " + filterStr);
            if (filterStr.matches("[0-9]")) {
              return new NumberFilter(Double.valueOf(filterStr));
            }
            if (filterStr.matches(INDICATOR_PATTERN_STR)) {
              return new IndicatorFilter()
                .setIndicator(IndicatorEnum.valueOf(Character.toString(filterStr.charAt(filterStr.indexOf("]") + 1))))
                .setOffset(Integer.valueOf(filterStr.substring(filterStr.indexOf(".")+1, filterStr.length())));
            }
            if (filterStr.matches(FUNCTION_PATTERN_STR)) {
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



  @Test
  public void testParseLine() {

    parseLine("[d]C.1 = [d]AVGC10.2 ");

  }

  // private String parseWord(String word) {
  // // System.out.println(value);

  // Matcher matcher = OHLCV_PATTERN.matcher(value);
  // if (matcher.find()) {
  // criteria.setIndicator(IndicatorEnum.valueOf(matcher.group(1)))
  // .setOffset(Integer.valueOf(matcher.group(2)));

  // System.out.println("OHLCV_PATTERN : " + matcher.group(1));

  // continue;
  // }

  // matcher = AVGMINMAX_PATTERN.matcher(value);
  // if (matcher.find()) {

  // System.out.println("AVGMINMAX_PATTERN: " + matcher.group(1));

  // criteria.setIndicator(IndicatorEnum.valueOf(matcher.group(1)))
  // .setOffset(Integer.valueOf(matcher.group(2)));
  // }

  // System.err.println("no match: " + value);
  // return "";
  // }

  @Test
  public void testParseQuery() {

    String query = "[d]C.0 > [d]AVGC10.0 " +
        "AND [d]C.0 > [d]AVGC20.0 " +
        "AND (" +
        "   [d]DV.0 > 3 " +
        "   OR [d]MINDV5.5 > 3" +
        ")";

  }

  private enum Ohlcv {
    O, H, L, C, V
  }

  private enum Indicator {
    AVGO, AVGH, AVGL, AVGC, AVGV,
    ATR,
    DV
  }

  private enum Operator {
    AND, OR
  }

  private enum CriteriaType {
    SINGLE("O", "H", "L", "C", "V"),
    OPERATOR("AND", "OR"),
    AVG("ATR", "AVG", "DV");

    private String[] possibleValues;

    CriteriaType(String... possibleValues) {
      this.possibleValues = possibleValues;
    }

    public static final CriteriaType fromValue(String value) {
      for (CriteriaType criteriaType : CriteriaType.values()) {
        if (Arrays.stream(criteriaType.possibleValues).anyMatch(s -> s.matches(value))) {
          return criteriaType;
        }
      }
      throw new UnsupportedOperationException("Invalid Criteria value: " + value);
    }
  }
}
