package com.rno.tickerscanner.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "pattern_match")
@Data
public class PatternMatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "pattern_name", nullable = false, length = 50)
    private String patternName;

    @Column(name = "pattern_time")
    private LocalDateTime patternTime;

    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternMatchEntity that = (PatternMatchEntity) o;
        return Objects.equals(symbol, that.symbol)
                && Objects.equals(patternName, that.patternName)
                // && Objects.equals(patternTime, that.patternTime)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                symbol,
                patternName
//                patternTime
        );
    }
}