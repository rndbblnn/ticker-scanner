package com.rndbblnn.tickerscanner;

import com.rndbblnn.tickerscanner.aql.AndOrEnum;
import com.rndbblnn.tickerscanner.aql.Criteria;
import com.rndbblnn.tickerscanner.aql.CriteriaGroup;
import com.rndbblnn.tickerscanner.dao.QueryRepository;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class QueryAsyncService {

  private final QueryRepository queryRepository;

  @Async
  public CompletableFuture<CriteriaGroup> process(CriteriaGroup criteriaGroup) {

    System.out.println(criteriaGroup.getTableName());
    if (queryRepository.existsUnloggedTable(criteriaGroup.getTableName())) {
      log.info("\t criteriaGroup {}: already exists. Returning it. ", criteriaGroup.getName());
      return CompletableFuture.completedFuture(criteriaGroup);
    }

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
