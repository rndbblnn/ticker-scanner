package com.rndbblnn.tickerscanner.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PatternMatchDto {

  private String symbol;
  private LocalDateTime patternTime;

}
