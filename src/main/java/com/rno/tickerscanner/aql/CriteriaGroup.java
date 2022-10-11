package com.rno.tickerscanner.aql;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CriteriaGroup {

    private String name;
    
    private List<Object> criterias = new ArrayList<>();

    public String getTableName() {
        return "tmp_" + this.getName();
    }

    public CriteriaGroup add(Criteria criteria) {
        this.criterias.add(criteria);
        return this;
    }

    public CriteriaGroup add(AndOrEnum andOr) {
        this.criterias.add(andOr);
        return this;
    }

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
