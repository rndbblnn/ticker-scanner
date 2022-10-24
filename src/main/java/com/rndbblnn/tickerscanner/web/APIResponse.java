package com.rndbblnn.tickerscanner.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class APIResponse<T> {

  private T payload;

}
