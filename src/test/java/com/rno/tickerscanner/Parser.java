package com.rno.tickerscanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Parser {

  private static final Pattern OHLCV_PATTERN = Pattern.compile("^([O,H,L,C,V])\\.([0-9]+$)");
  private static final Pattern AVGMINMAX_PATTERN = Pattern.compile("^([A-Z]*)([0-9]+$)\\.([0-9]+$)");

  private String query;
  private List<CriteriaGroup> criteriaList = new ArrayList<>();

  public List<CriteriaGroup> build(String query) {
    this.query = query.replaceAll("\\s", "");

    var lineArr = query.split("\\R");

    CriteriaGroup criteriaGroup = new CriteriaGroup();

    for (String line : lineArr) {
      if (line.startsWith("(")) {
        parseLine(line)
            .stream()
            .forEach(criteriaGroup::addFilter);
      }
    }

    return criteriaList;
  }

  private static List<Filter> parseLine(String line) {

    line = line.replaceAll("\\s", "");

    line.chars()
      .forEachOrdered(i -> {

        String s = Character.toString((char)i);


        if (s.matches("[A-Z]")) {

        }


      });


    Matcher matcher = Pattern.compile("(\\<\\=|\\=|\\<|\\>)|(([O,H,L,C,V])\\.([0-9]+$))")
        .matcher(line);

    // System.out.println(matcher.find());
    // System.out.println(matcher.groupCount());

    if (matcher.find()) {

      System.out.println("\tfind");

      for (int i = 0; i < matcher.groupCount(); i++) {

        System.out.println("\t\tgroup: " + i);
        System.out.println(matcher.group(i));
      }

    }

    return null;
  }

  @Test
  public void testParseLine() {

    parseLine("C.0 = [d]SMA10.0 ");

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

    String query = "[d]C.0 > [d]SMA10.0 " +
        "AND [d]C.0 > [d]SMA20.0 " +
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
