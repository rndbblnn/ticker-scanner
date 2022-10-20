package com.rno.tickerscanner.web;

import com.google.common.collect.Lists;
import com.rno.tickerscanner.QueryService;
import com.rno.tickerscanner.dto.PatternMatchDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class QueryController {

  private final QueryService queryService;

  @GetMapping("")
  public APIResponse<String> index() {
    return APIResponse.<String>builder()
        .payload("sup")
        .build();
  }

  @GetMapping("/search")
  @SneakyThrows
  public APIResponse<List<PatternMatchDto>> getSearch(@RequestParam String q, @RequestParam(required = false) Boolean mock) {

//    log.info("query:\n{}", q);

    var results = BooleanUtils.isTrue(mock)
        ? MOCK_RESPONSE_LIST
        : queryService.search(q);

    log.info("Returning {} PatternMatches", results.size());

    return APIResponse.<List<PatternMatchDto>>builder()
        .payload(results)
        .build();

  }

  private static final List<PatternMatchDto> MOCK_RESPONSE_LIST =
      Lists.newArrayList(
          new PatternMatchDto()
              .setPatternTime(LocalDateTime.now().minusDays(100)
                  .withHour(0)
                  .withMinute(0)
                  .withSecond(0)
                  .withNano(0))
              .setSymbol("TSLA"),
          new PatternMatchDto()
              .setPatternTime(LocalDateTime.now().minusDays(50).withHour(0)
                  .withMinute(0)
                  .withSecond(0)
                  .withNano(0))
              .setSymbol("ENPH")
      );

}
