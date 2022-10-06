package com.rno.tickerscanner.aql;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Criteria {

    private Filter left;
    private OperatorEnum operator;
    private Filter right;
    
}