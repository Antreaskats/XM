package com.xm.technical.cryptoinvestment.service;

import com.xm.technical.cryptoinvestment.model.Crypto;
import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CalculateCryptoDetailsService {
    public List<CryptoDetails> calculateDetailsList(Map<String, List<Crypto>> cryptoData) {
        List<CryptoDetails> cryptoDetailsList = new ArrayList<>();
        cryptoData.forEach((cryptoName, data) -> {
            if (!data.isEmpty()) {
                BigDecimal oldest = data.stream().min(Comparator.comparing(Crypto::getTimestamp)).get().getPrice();
                BigDecimal newest = data.stream().max(Comparator.comparing(Crypto::getTimestamp)).get().getPrice();
                BigDecimal max = data.stream().max(Comparator.comparing(Crypto::getPrice)).get().getPrice();
                BigDecimal min = data.stream().min(Comparator.comparing(Crypto::getPrice)).get().getPrice();
                cryptoDetailsList.add(new CryptoDetails(data.get(0).getSymbol(), oldest, newest, min, max));
            }
        });
        return cryptoDetailsList;
    }
}
