package com.rno.tickerscanner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NumberFilter implements Filter {

    private double number;
    
}
