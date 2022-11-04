package com.rndbblnn.tickerscanner.web;

import com.rndbblnn.stonks.commons.entity.CandleDailyEntity;
import com.rndbblnn.tickerscanner.dao.CandleDailyRepository;
import com.rndbblnn.tickerscanner.dao.StatRepository;
import com.rndbblnn.tickerscanner.dto.TopTradeDto;
import com.rndbblnn.tickerscanner.web.dto.APIResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StatController {

  private final StatRepository statRepository;
  private final CandleDailyRepository candleDailyRepository;

  @GetMapping("/topshortsoftheday")
  public APIResponse<List<TopTradeDto>> getTopShortsOfTheDay() {

    CandleDailyEntity latestCandle = candleDailyRepository.findLatestByTickerName("TSLA").get();

    return APIResponse.<List<TopTradeDto>>builder()
        .payload(
            statRepository.getTopShortsOfTheDay(
                latestCandle.getTickTime().toLocalDate(),
                20
            )
        )
        .build();
  }

  @GetMapping("/toplongsoftheday")
  public APIResponse<List<TopTradeDto>> getTopLongsOfTheDay() {

    CandleDailyEntity latestCandle = candleDailyRepository.findLatestByTickerName("TSLA").get();

    return APIResponse.<List<TopTradeDto>>builder()
        .payload(
            statRepository.getTopLongsOfTheDay(
                latestCandle.getTickTime().toLocalDate(),
                20
            )
        )
        .build();
  }

}
