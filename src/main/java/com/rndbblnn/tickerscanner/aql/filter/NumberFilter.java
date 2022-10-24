package com.rndbblnn.tickerscanner.aql.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NumberFilter implements Filter {

    private double number;
    
}
