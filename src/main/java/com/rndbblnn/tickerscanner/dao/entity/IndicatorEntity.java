package com.rndbblnn.tickerscanner.dao.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class IndicatorEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "symbol", nullable = false)
  private String symbol;

  @Column(name = "tick_time", nullable = false)
  private LocalDateTime tickTime;

}
