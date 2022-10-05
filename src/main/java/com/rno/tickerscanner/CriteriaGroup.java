package com.rno.tickerscanner;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CriteriaGroup {
    
    private List<Criteria> criterias = new ArrayList<>();

    // public CriteriaGroup addFilter(Filter filter) {

    //     if (!filters.isEmpty()) {
    //         Object lastFilter = filters.get(filters.size()-1);
    //         if (lastFilter.getClass() == filter.getClass() ) {
    //             throw new RuntimeException(filter.getClass() + " == " + lastFilter.getClass());
    //         }

    //     }

    //     this.filters.add(filter);
    //     return this;
    // }
}
