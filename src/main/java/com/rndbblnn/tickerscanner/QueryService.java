package com.rndbblnn.tickerscanner;

import com.rndbblnn.stonks.commons.utils.DateUtils;
import com.rndbblnn.tickerscanner.aql.Criteria;
import com.rndbblnn.tickerscanner.aql.CriteriaGroup;
import com.rndbblnn.tickerscanner.aql.Parser;
import com.rndbblnn.tickerscanner.aql.filter.ArithmeticFilter;
import com.rndbblnn.tickerscanner.aql.filter.Filter;
import com.rndbblnn.tickerscanner.aql.filter.IndicatorFilter;
import com.rndbblnn.tickerscanner.crunch.CrunchService;
import com.rndbblnn.tickerscanner.dao.QueryRepository;
import com.rndbblnn.tickerscanner.dto.PatternMatchDto;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class QueryService {

  private final CrunchService crunchService;
  private final QueryRepository queryRepository;
  private final QueryAsyncService queryAsyncService;

  @SneakyThrows
//  @Cacheable(cacheNames = "QueryService-search")
  public List<PatternMatchDto> search(String queryStr) throws InterruptedException {

//    queryRepository.dropAllTempTables();

    List<Object> queryList = Parser.builder()
        .query(queryStr)
        .build();

    crunchService.prepare();

    List<CriteriaGroup> allCriteriaGroups =
        queryList.stream().filter(CriteriaGroup.class::isInstance)
            .map(CriteriaGroup.class::cast)
            .collect(Collectors.toList());

    List<Criteria> allCriterias =
        queryList.stream().filter(CriteriaGroup.class::isInstance)
            .map(CriteriaGroup.class::cast)
            .map(CriteriaGroup::getCriterias)
            .flatMap(Collection::stream)
            .filter(Criteria.class::isInstance)
            .map(Criteria.class::cast)
            .collect(Collectors.toList());

    Set<Filter> allFilterSet =
        allCriterias.stream()
            .map(Criteria.class::cast)
            .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
            .filter(IndicatorFilter.class::isInstance)
            .map(IndicatorFilter.class::cast)
            .collect(Collectors.toSet());

    allFilterSet.addAll(
        allCriterias.stream()
            .map(Criteria.class::cast)
            .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
            .filter(ArithmeticFilter.class::isInstance)
            .map(ArithmeticFilter.class::cast)
            .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
            .filter(IndicatorFilter.class::isInstance)
            .map(IndicatorFilter.class::cast)
            .collect(Collectors.toList())
    );

    CompletableFuture.allOf(
        allFilterSet
            .stream()
            .distinct()
            .map(crunchService::crunch)
            .toArray(arr -> new CompletableFuture[allFilterSet.size()])
    ).join();

    String timestampStr = LocalDateTime.now().format(DateUtils.ONLYDIGITS_NOTIME_DATEFORMAT);
    for (CriteriaGroup criteriaGroup : allCriteriaGroups) {
      criteriaGroup.setName("cg_" + timestampStr + "_" + String.valueOf(criteriaGroup.getCriterias().hashCode()).replaceAll("\\-", "D"));
    }

    List<CompletableFuture<CriteriaGroup>> completableFutureList =
        allCriteriaGroups.stream()
            .map(queryAsyncService::process)
            .collect(Collectors.toList());

    CompletableFuture.allOf(
        completableFutureList.stream()
            .toArray(arr -> new CompletableFuture[completableFutureList.size()])
    ).join();

    // intersect all CGs
    List<PatternMatchDto> patternMatchDtoList =
        queryRepository.intersectAll(queryList)
            .stream()
            .filter(patternMatchDto -> patternMatchDto.getPatternTime().isAfter(LocalDateTime.of(2019,3,1,0,0)))
            .filter(patternMatchDto -> patternMatchDto.getPatternTime().isBefore(LocalDateTime.now().minusMonths(2)))
            .collect(Collectors.toList());

    return patternMatchDtoList;
  }

}
