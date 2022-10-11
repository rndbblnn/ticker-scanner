package com.rno.tickerscanner;

import com.rno.tickerscanner.aql.AndOrEnum;
import com.rno.tickerscanner.aql.Criteria;
import com.rno.tickerscanner.aql.CriteriaGroup;
import com.rno.tickerscanner.dao.QueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
public class QueryAsyncService {

  private final QueryRepository queryRepository;

  @Async
  public CompletableFuture<CriteriaGroup> process(CriteriaGroup criteriaGroup) {

    log.info("\t criteriaGroup {}: processing... ", criteriaGroup.getName());

    queryRepository.createTempTable(criteriaGroup);

    AndOrEnum criteriaAndOr = null;
    for (int criteriaIdx = 0; criteriaIdx < criteriaGroup.getCriterias().size(); criteriaIdx++) {
      Object critObj = criteriaGroup.getCriterias().get(criteriaIdx);
      if (critObj instanceof AndOrEnum) {
        criteriaAndOr = (AndOrEnum) critObj;
        continue;
      }
      if (criteriaAndOr == null || criteriaAndOr.equals(AndOrEnum.OR)) {
        log.info("\t{} insert criteria {}", criteriaGroup.getName(), critObj);
        queryRepository.insertCriteria(criteriaGroup, (Criteria) critObj);
        continue;
      }

      log.info("\t{} delete criteria {}", criteriaGroup.getName(), critObj);
      queryRepository.deleteCriteria(criteriaGroup, (Criteria) critObj);
    }

    return CompletableFuture.completedFuture(criteriaGroup);

  }
}
