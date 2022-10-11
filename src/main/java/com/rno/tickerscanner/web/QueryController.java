package com.rno.tickerscanner.web;

import com.rno.tickerscanner.QueryService;
import com.rno.tickerscanner.crunch.CrunchService;
import com.rno.tickerscanner.dao.QueryRepository;
import com.rno.tickerscanner.dto.PatternMatchDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class QueryController {

  private final QueryService queryService;
  private final CrunchService crunchService;
  private final QueryRepository queryRepository;

  @GetMapping("/search")
  @SneakyThrows
  public APIResponse<List<PatternMatchDto>> getSearch(@RequestParam String q) {

    return APIResponse.<List<PatternMatchDto>>builder()
        .payload(queryService.search(q))
        .build();

  }

}
