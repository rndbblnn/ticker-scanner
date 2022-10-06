package com.rno.tickerscanner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatternMatchDto {

  private String symbol;
  private LocalDateTime patternTime;

}
