package com.rno.tickerscanner;

import com.rno.tickerscanner.aql.Criteria;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.aql.Filter;
import com.rno.tickerscanner.aql.IndicatorFilter;
import com.rno.tickerscanner.aql.Parser;
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
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class QueryService {

  private final CrunchService crunchService;
  private final QueryRepository queryRepository;

  @SneakyThrows
  public List<PatternMatchDto> search(String queryStr) throws InterruptedException {

    List<Object> queryList = Parser.builder()
        .query(queryStr)
        .build();

    crunchService.prepare();

    List<Criteria> allCriterias =
        queryList.stream().filter(CriteriaGroup.class::isInstance)
            .map(CriteriaGroup.class::cast)
            .map(CriteriaGroup::getCriterias)
            .flatMap(Collection::stream)
            .filter(Criteria.class::isInstance)
            .map(Criteria.class::cast)
            .collect(Collectors.toList());

    Set<Filter> filterSet =
        allCriterias.stream()
            .map(Criteria.class::cast)
            .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
            .filter(IndicatorFilter.class::isInstance)
            .map(IndicatorFilter.class::cast)
            .collect(Collectors.toSet());

    crunchService.crunchAll(filterSet);

    allCriterias.stream()
        .forEach(criteria -> {
          try {
            criteria.setResults(
                queryRepository.findPatternMatches(
                    criteria.getLeft(),
                    criteria.getOperator(),
                    criteria.getRight()));
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });


    CompletableFuture.allOf(
        allCriterias.stream()
            .map(Criteria::getResults)
            .toArray(arr -> new CompletableFuture[allCriterias.size()])
    ).join();

    allCriterias.stream()
        .forEach(criteria -> {
          System.out.println(criteria);
          try {
            System.out.println(criteria.getResults() != null ? criteria.getResults().get().size() :  0);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          } catch (ExecutionException e) {
            throw new RuntimeException(e);
          }
        });

    return null;
  }


}
