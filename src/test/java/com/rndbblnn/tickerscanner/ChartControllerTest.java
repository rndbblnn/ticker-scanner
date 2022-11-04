package com.rndbblnn.tickerscanner;

import com.rndbblnn.stonks.commons.dto.CandleDto;
import com.rndbblnn.tickerscanner.web.dto.APIResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;


public class ChartControllerTest extends BaseIntegrationTest {

  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Test
  public void getChart() {
    this.restTemplate.exchange(
            "http://localhost:" + port + "/chart/TSLA/15m?from=2022-10-28&to=2022-10-31",
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<APIResponse<List<CandleDto>>>() {
            })
        .getBody().getPayload()
        .stream()
        .forEach(System.out::println);
  }
}
