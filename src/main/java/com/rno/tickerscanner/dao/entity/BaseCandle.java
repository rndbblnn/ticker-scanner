package com.rno.tickerscanner.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseCandle {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  @ToString.Exclude
  private Long id;

  @Column(name = "symbol")
  private String symbol;

  @Column(name = "tick_time")
  private LocalDateTime tickTime;

  @Column(name ="created")
  private LocalDateTime created;

  @Column(name ="open_price")
  private Double open;

  @Column(name ="high_price")
  private Double high;

  @Column(name ="low_price")
  private Double low;

  @Column(name ="close_price")
  private Double close;

  @Column(name ="volume")
  private Long volume;

}
