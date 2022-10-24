package com.rno.tickerscanner.aql;

import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.dto.PatternMatchDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Criteria {

    private Filter left;
    private OperatorEnum operator;
    private Filter right;

    @EqualsAndHashCode.Exclude
    private transient CompletableFuture<List<PatternMatchDto>> results;
}
