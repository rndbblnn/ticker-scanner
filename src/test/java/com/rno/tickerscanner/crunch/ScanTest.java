package com.rno.tickerscanner.crunch;

import com.rno.tickerscanner.BaseTest;
import com.rno.tickerscanner.Criteria;
import com.rno.tickerscanner.IndicatorEnum;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CruncherTest extends BaseTest {

  @Autowired
  private Cruncher cruncher;

  @Test
  public void testCrunch(){
    cruncher.prepare();

    Lists.newArrayList(
        new Criteria(IndicatorEnum.SMA, 10, 0)
//        ,new Criteria(IndicatorEnum.ATR, 20, 0)
    ).forEach(
        c -> cruncher.crunchCriteria(c)
    );

  }

}
