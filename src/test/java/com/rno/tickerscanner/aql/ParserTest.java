package com.rno.tickerscanner.aql;

import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.aql.filter.IndicatorFilter;
import com.rno.tickerscanner.aql.filter.NumberFilter;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParserTest {

  public static final String ARITHMETIC_TEST_QUERY =
      "[d]C.0 / [d]C.1 >= 1.1";

  public static final String TEST_QUERY =
      "[d]C.1 > [d]AVGC10.2 \n" +
          "AND [d]C.0 <= [d]AVGC20.0 \n" +
          "AND\n" +
          "(\n" +
          "   [d]DV.1 > 3 \n" +
          "   OR [d]MINDV5.5 > 3\n" +
          ")\n";

  @Test
  public void testParseDailyCloseEqualAvgc10() {
    List<Object> query =
        Parser.builder()
            .query("[d]C.1 = [d]AVGC10.2 ")
            .build();

    Assert.assertEquals(1, query.size());

    CriteriaGroup criteriaGroup = ((CriteriaGroup) query.get(0));
    Assert.assertEquals(1, criteriaGroup.getCriterias().size());

    Filter leftFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getLeft();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.C, ((IndicatorFilter) leftFilter).getIndicator());
    Assert.assertEquals(1, ((IndicatorFilter) leftFilter).getOffset());

    OperatorEnum operator = ((Criteria) criteriaGroup.getCriterias().get(0)).getOperator();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(OperatorEnum.EQUAL, operator);

    Filter rightFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getRight();
    Assert.assertTrue(rightFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.AVGC, ((IndicatorFilter) rightFilter).getIndicator());
    Assert.assertEquals(10, ((IndicatorFilter) rightFilter).getRange());
    Assert.assertEquals(2, ((IndicatorFilter) rightFilter).getOffset());
  }

  @Test
  public void testParseQuery() {

    List<Object> query =
        Parser.builder()
            .query(TEST_QUERY)
            .build();

    // CriteriaGroup 1
    Assert.assertEquals(3, query.size());

    CriteriaGroup criteriaGroup = ((CriteriaGroup) query.get(0));
    Assert.assertEquals(3, criteriaGroup.getCriterias().size());

    // - Criteria 1
    Filter leftFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getLeft();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.C, ((IndicatorFilter) leftFilter).getIndicator());
    Assert.assertEquals(1, ((IndicatorFilter) leftFilter).getOffset());

    OperatorEnum operator = ((Criteria) criteriaGroup.getCriterias().get(0)).getOperator();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(OperatorEnum.GREATER, operator);

    Filter rightFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getRight();
    Assert.assertTrue(rightFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.AVGC, ((IndicatorFilter) rightFilter).getIndicator());
    Assert.assertEquals(10, ((IndicatorFilter) rightFilter).getRange());
    Assert.assertEquals(2, ((IndicatorFilter) rightFilter).getOffset());

    // - AndOrEnum 2
    Assert.assertEquals(AndOrEnum.AND, criteriaGroup.getCriterias().get(1));

    // - Criteria 3
    leftFilter = ((Criteria) criteriaGroup.getCriterias().get(2)).getLeft();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.C, ((IndicatorFilter) leftFilter).getIndicator());
    Assert.assertEquals(0, ((IndicatorFilter) leftFilter).getOffset());

    operator = ((Criteria) criteriaGroup.getCriterias().get(2)).getOperator();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(OperatorEnum.LESSER_OR_EQUAL, operator);

    rightFilter = ((Criteria) criteriaGroup.getCriterias().get(2)).getRight();
    Assert.assertTrue(rightFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.AVGC, ((IndicatorFilter) rightFilter).getIndicator());
    Assert.assertEquals(20, ((IndicatorFilter) rightFilter).getRange());
    Assert.assertEquals(0, ((IndicatorFilter) rightFilter).getOffset());

    Assert.assertEquals(AndOrEnum.AND, query.get(1));

    // -------------------------------------

    // CriteriaGroup 2
    criteriaGroup = ((CriteriaGroup) query.get(2));
    Assert.assertEquals(3, criteriaGroup.getCriterias().size());

    // - Criteria 1
    leftFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getLeft();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.DV, ((IndicatorFilter) leftFilter).getIndicator());
    Assert.assertEquals(1, ((IndicatorFilter) leftFilter).getOffset());

    operator = ((Criteria) criteriaGroup.getCriterias().get(0)).getOperator();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(OperatorEnum.GREATER, operator);

    rightFilter = ((Criteria) criteriaGroup.getCriterias().get(0)).getRight();
    Assert.assertTrue(rightFilter instanceof NumberFilter);
    Assert.assertEquals(3.0d, ((NumberFilter) rightFilter).getNumber(), 0d);

    // - AndOrEnum 2
    Assert.assertEquals(AndOrEnum.OR, criteriaGroup.getCriterias().get(1));

    // - Criteria 3
    leftFilter = ((Criteria) criteriaGroup.getCriterias().get(2)).getLeft();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(IndicatorEnum.MINDV, ((IndicatorFilter) leftFilter).getIndicator());
    Assert.assertEquals(5, ((IndicatorFilter) leftFilter).getRange());
    Assert.assertEquals(5, ((IndicatorFilter) leftFilter).getOffset());

    operator = ((Criteria) criteriaGroup.getCriterias().get(2)).getOperator();
    Assert.assertTrue(leftFilter instanceof IndicatorFilter);
    Assert.assertEquals(OperatorEnum.GREATER, operator);

    rightFilter = ((Criteria) criteriaGroup.getCriterias().get(2)).getRight();
    Assert.assertTrue(rightFilter instanceof NumberFilter);
    Assert.assertEquals(3.0d, ((NumberFilter) rightFilter).getNumber(), 0d);

  }


  @Test
  public void testParse_arithmeticCriteria() {
    List<Object> query =
        Parser.builder()
            .query(ARITHMETIC_TEST_QUERY)
            .build();
  }


}
