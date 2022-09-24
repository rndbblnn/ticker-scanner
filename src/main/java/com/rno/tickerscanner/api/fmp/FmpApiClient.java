package com.rno.tickerscanner.api.fmp;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FmpApiClient {

    private static final String BASE_URL = "https://financialmodelingprep.com/api/";
    private static final String API_KEY = "6596e26bc37c3a375e6a113d15c4fb03";
    public static final DateTimeFormatter DATE_TIME_DAILY_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    private WebClient webClient = WebClient.create(BASE_URL);

    public HistoricalChartFull getHistoricalPriceFull(String symbol, LocalDateTime from, LocalDateTime to) {
        HistoricalChartFull historicalChartList =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v3/historical-price-full/{symbol}")
                                .queryParam("apikey", API_KEY)
                                .queryParam("from", from.format(DATE_TIME_DAILY_FORMATTER))
                                .queryParam("to", to.format(DATE_TIME_DAILY_FORMATTER))
                                .build(symbol))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(HistoricalChartFull.class)
                        .block();

        return historicalChartList;
    }

    public Profile getProfile(String symbol) {
        List<Profile> profileList =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v3/profile/{symbol}")
                                .queryParam("apikey", API_KEY)
                                .build(symbol))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Profile>>() {
                        })
                        .block();

        if (CollectionUtils.isEmpty(profileList)) {
            return null;
        }

        return profileList.iterator().next();
    }

    public SharesFloat getSharesFloat(String symbol) {
        List<SharesFloat> sharesFloatList =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v4/shares_float")
                                .queryParam("symbol", symbol)
                                .queryParam("apikey", API_KEY)
                                .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<SharesFloat>>() {
                        })
                        .block();
        if (CollectionUtils.isEmpty(sharesFloatList)) {
            return null;
        }

        return sharesFloatList.iterator().next();
    }

    public MarketCap getLatestMarketCap(String symbol) {
        List<MarketCap> marketCapList =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v3/historical-market-capitalization/{symbol}")
                                .queryParam("apikey", API_KEY)
                                .build(symbol))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<MarketCap>>() {
                        })
                        .block();

        if (CollectionUtils.isEmpty(marketCapList)) {
            return null;
        }

        return marketCapList.iterator().next();
    }

    public List<MarketCap> getHistoricalMarketCap(String symbol, final int limit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/historical-market-capitalization/{symbol}")
                        .queryParam("apikey", API_KEY)
                        .queryParam("limit", (limit == 0 ? 1000 : limit))
                        .build(symbol))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MarketCap>>() {
                })
                .block();
    }


    // no date filtering, returns only last 20 days
    public List<HistoricalChart> getHistoricalChart5Min(String symbol, LocalDateTime from, LocalDateTime to) {
        List<HistoricalChart> historicalChartList =
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v3/historical-chart/5min/{symbol}")
                                .queryParam("apikey", API_KEY)
                                .queryParam("from", from.format(DATE_TIME_FORMATTER))
                                .queryParam("to", to.format(DATE_TIME_FORMATTER))
                                .build(symbol))
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<HistoricalChart>>() {
                        })
                        .block();

        return historicalChartList;
    }


}
