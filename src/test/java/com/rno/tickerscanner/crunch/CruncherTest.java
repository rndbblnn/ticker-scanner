package com.rno.tickerscanner.crunch;

import com.rno.tickerscanner.BaseTest;
import com.rno.tickerscanner.Criteria;
import com.rno.tickerscanner.IndicatorEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CruncherTest extends BaseTest {

  @Autowired
  private Cruncher cruncher;

  @Test
  public void testCrunch(){
    cruncher.crunch(new Criteria(IndicatorEnum.ATR, 20, 0));
  }

}
