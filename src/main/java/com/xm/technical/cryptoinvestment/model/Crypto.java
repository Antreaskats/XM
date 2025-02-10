package com.xm.technical.cryptoinvestment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@NoArgsConstructor
public class Crypto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String timestamp;
    private String symbol;
    private BigDecimal price;



    @JsonCreator
    public Crypto(@JsonProperty("timestamp") String timestamp, @JsonProperty("symbol") String symbol, @JsonProperty("price") BigDecimal price){
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.price = price;
    }

    public LocalDate transformTimestamp(){
        Long epochSeconds = Long.valueOf(timestamp);
        return Instant.ofEpochMilli(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
