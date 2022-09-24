package com.rno.tickerscanner.dao.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticks")
@Data
@ToString
public class TickEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @ToString.Exclude
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "timeframe")
    private String timeframe;

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

    @Column(name ="sma6")
    private Double sma6;

    @Column(name ="sma10")
    private Double sma10;

    @Column(name ="sma20")
    private Double sma20;

    @Column(name ="sma50")
    private Double sma50;

    @Column(name ="sma100")
    private Double sma100;

    @Column(name ="sma200")
    private Double sma200;

    @Column(name ="dv_buying")
    private Double DVBuying;

    @Column(name ="dv_buying_sma20")
    private Double DVBuyingSma20;

    @Column(name ="dv_selling")
    private Double DVSelling;

    @Column(name ="dv_selling_sma20")
    private Double DVSellingSma20;

    @Column(name ="rti3")
    private Double RTI3;

    @Column(name ="rti4")
    private Double RTI4;

    @Column(name ="rti5")
    private Double RTI5;

    @Column(name ="rti6")
    private Double RTI6;

    @Column(name ="three_inside_days")
    private Double threeInsideDays;

    @Column(name ="ADR")
    private Double ADR;

    @Column(name ="ATR20")
    private Double ATR20;

    @Column(name ="market_cap")
    private Double marketCap;

    @Column(name ="shares_outstanding")
    private Long sharesOutstanding;

    @Column(name ="shares_float")
    private Double sharesFloat;

    @Column(name ="eps_last_qtr")
    private Double epsLastQtr;

    @Column(name ="iwm_close_price")
    private Double IWMClose;

    @Column(name ="iwm_sma10")
    private Double IWMmovingAverage10;

    @Column(name ="iwm_sma20")
    private Double IWMmovingAverage20;

    @Column(name ="iwm_sma50")
    private Double IWMmovingAverage50;

    @Column(name ="iwm_di_positive")
    private Double IWMPositiveDirectionalMovement100;

    @Column(name ="iwm_di_negative")
    private Double IWMNegativeDirectionalMovement100;

}
