package com.xm.technical.cryptoinvestment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoNormalizedRange {
    private String name;
    private BigDecimal normalizedRange;
}
