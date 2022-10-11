package com.rno.tickerscanner.aql;

import com.rno.tickerscanner.aql.filter.Filter;
import com.rno.tickerscanner.dto.PatternMatchDto;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Data
public class Criteria {

    private Filter left;
    private OperatorEnum operator;
    private Filter right;

    private transient CompletableFuture<List<PatternMatchDto>> results;

}
