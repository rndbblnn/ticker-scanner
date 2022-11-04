package com.rndbblnn.tickerscanner;

import com.rndbblnn.tickerscanner.dto.TopTradeDto;
import com.rndbblnn.tickerscanner.web.dto.APIResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;


public class StatControllerTest extends BaseIntegrationTest {
  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Test
  public void testGetTopShortsOfTheDay() {
    this.restTemplate.exchange(
            "http://localhost:" + port + "/topshortsoftheday",
            HttpMethod.GET,
            new HttpEntity<>(null),
            new ParameterizedTypeReference<APIResponse<List<TopTradeDto>>>() {
            })
        .getBody().getPayload()
        .stream()
        .forEach(System.out::println);
  }
}
