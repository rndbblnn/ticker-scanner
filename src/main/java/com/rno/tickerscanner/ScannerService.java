package com.rno.tickerscanner;

import com.rno.tickerscanner.aql.Criteria;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.aql.Filter;
import com.rno.tickerscanner.aql.IndicatorFilter;
import com.rno.tickerscanner.aql.Parser;
import com.rno.tickerscanner.crunch.CrunchService;
import com.rno.tickerscanner.dto.PatternMatchDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ScannerService {

  private final CrunchService crunchService;

  public List<PatternMatchDto> scan(String queryStr) {

    List<Object> queryList = Parser.builder()
        .query(queryStr)
        .build();

    crunchService.prepare();

    Set<Filter> filterSet =
        queryList.stream().filter(CriteriaGroup.class::isInstance)
            .map(CriteriaGroup.class::cast)
            .map(CriteriaGroup::getCriterias)
            .flatMap(Collection::stream)
            .filter(Criteria.class::isInstance)
            .map(Criteria.class::cast)
            .flatMap(c -> Stream.of(c.getLeft(), c.getRight()))
            .filter(IndicatorFilter.class::isInstance)
            .map(IndicatorFilter.class::cast)
            .collect(Collectors.toSet());

    crunchService.crunchAll(filterSet);

    for (Object obj : queryList) {
      if (obj instanceof CriteriaGroup) {
        CriteriaGroup criteriaGroup = (CriteriaGroup) obj;

        criteriaGroup
            .getCriterias()
            .forEach(criteria -> {

            });
      }
    }
    return null;
  }


}
