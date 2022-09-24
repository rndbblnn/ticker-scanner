package com.rno.tickerscanner.api.fmp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Profile {
    public String symbol;
    public double price;
    public double beta;
    public long volAvg;
    public long mktCap;
    public double lastDiv;
    public String range;
    public double changes;
    public String companyName;
    public String currency;
    public String cik;
    public String isin;
    public String cusip;
    public String exchange;
    public String exchangeShortName;
    public String industry;
    public String website;
    public String description;
    public String ceo;
    public String sector;
    public String country;
    public String fullTimeEmployees;
    public String phone;
    public String address;
    public String city;
    public String state;
    public String zip;
    public double dcfDiff;
    public double dcf;
    public String image;

    @JsonFormat(pattern="yyyy-MM-dd")
    public LocalDate date;
    public String ipoDate;
    public boolean defaultImage;
    public boolean isEtf;
    public boolean isActivelyTrading;
    public boolean isAdr;
    public boolean isFund;
}
