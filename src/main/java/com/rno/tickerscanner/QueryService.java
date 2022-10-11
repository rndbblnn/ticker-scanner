package com.rno.tickerscanner;

import com.rno.tickerscanner.aql.Criteria;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.aql.Parser;
import com.rno.tickerscanner.aql.filter.ArithmeticFilter;
import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.aql.filter.IndicatorFilter;
import com.rno.tickerscanner.crunch.CrunchService;
import com.rno.tickerscanner.dao.QueryRepository;
import com.rno.tickerscanner.dto.PatternMatchDto;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class QueryService {

  private final CrunchService crunchService;
  private final QueryRepository queryRepository;
  private final QueryAsyncService queryAsyncService;

  @SneakyThrows
  public List<PatternMatchDto> search(String queryStr) throws InterruptedException {

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
            .map(crunchService::crunch)
            .toArray(arr -> new CompletableFuture[allFilterSet.size()])
    );

    // get all CGs and iterate
    // - create temp table
    // - for each criteria
    // -- insert 1st + delete each in temp table
    // - join all temp tables

    long timeinmillis = System.currentTimeMillis();
    int criteriaGroupIdx = 0;
    for (CriteriaGroup criteriaGroup : allCriteriaGroups) {
      criteriaGroup.setName("cg_" + criteriaGroupIdx++ + "_" + timeinmillis);
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
        queryRepository.intersectAll(queryList);

    System.out.println(patternMatchDtoList);







//
//    allCriterias.stream()
//        .forEach(criteria -> {
//          try {
//            criteria.setResults(
//                queryRepository.findPatternMatches(
//                    criteria.getLeft(),
//                    criteria.getOperator(),
//                    criteria.getRight()));
//          } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//          }
//        });
//
//
//    CompletableFuture.allOf(
//        allCriterias.stream()
//            .map(Criteria::getResults)
//            .toArray(arr -> new CompletableFuture[allCriterias.size()])
//    ).join();
//
//    allCriterias.stream()
//        .forEach(criteria -> {
//          System.out.println(criteria);
//          try {
//            System.out.println(criteria.getResults() != null ? criteria.getResults().get().size() :  0);
//          } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//          } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//          }
//        });

    return patternMatchDtoList;
  }

}
