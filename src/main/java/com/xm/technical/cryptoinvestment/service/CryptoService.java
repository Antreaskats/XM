package com.xm.technical.cryptoinvestment.service;

import com.xm.technical.cryptoinvestment.exception.CryptoNotFoundException;
import com.xm.technical.cryptoinvestment.exception.CryptoNotSupportedException;
import com.xm.technical.cryptoinvestment.model.Crypto;
import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import com.xm.technical.cryptoinvestment.model.CryptoNormalizedRange;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CryptoService{
    private final PriceReaderService pricesReaderService;
    private final CalculateCryptoDetailsService calculateCryptoDetailsService;

    public CryptoService(PriceReaderService pricesReaderService, CalculateCryptoDetailsService calculateCryptoDetailsService){
        this.pricesReaderService = pricesReaderService;
        this.calculateCryptoDetailsService = calculateCryptoDetailsService;
    }

    public List<CryptoNormalizedRange> getSortedNormalizedRange() {
        Map<String, List<Crypto>> cryptoData = pricesReaderService.getCryptoData();
        if(cryptoData.isEmpty()){
            throw new CryptoNotFoundException("No cryptos found");
        }
        List<CryptoDetails> cryptoValues = calculateCryptoDetailsService.calculateDetailsList(cryptoData);
        return cryptoValues.stream()
                .map(entry ->
                    findNormalizedRange(entry.getCryptoName(), entry.getMax(), entry.getMin()))
                .sorted(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange).reversed())
                .collect(Collectors.toList());
    }

    public CryptoDetails calculateCryptoDetails(String cryptoName){
        Map<String, List<Crypto>> cryptoData = pricesReaderService.getCryptoData();
        return calculateCryptoDetailsService.calculateDetailsList(cryptoData).stream()
                .filter(value->value.getCryptoName().equals(cryptoName)).findFirst()
                .orElseThrow(()-> new CryptoNotSupportedException(cryptoName + " crypto is not yet supported yet"));
    }

    public CryptoNormalizedRange getHighestNormalizedRangeForThisDay(LocalDate date){
        Map<String, List<Crypto>> cryptoData = pricesReaderService.getCryptoData();
        List<CryptoDetails> cryptoDetailsList = calculateCryptoDetailsService.calculateDetailsList(cryptoData);
        Optional<CryptoNormalizedRange> highest =  cryptoData.values().stream()
                .flatMap(List::stream)
                .filter(crypto -> crypto.transformTimestamp().isEqual(date))
                .map(crypto ->
                    findNormalizedRange(crypto.getSymbol(), crypto.getPrice(), cryptoDetailsList.stream().filter(value -> value.getCryptoName().equals(crypto.getSymbol())).findFirst().get().getMin()))
                .max(Comparator.comparing(CryptoNormalizedRange::getNormalizedRange));
        if(highest.isEmpty()){
            throw new CryptoNotFoundException("No crypto normalized range found for this day");
        }
        return highest.get();
    }

    private CryptoNormalizedRange findNormalizedRange(String cryptoName, BigDecimal pricePoint, BigDecimal minPrice){
        return new CryptoNormalizedRange(cryptoName, (pricePoint.subtract(minPrice)).divide(minPrice, 5, RoundingMode.HALF_UP));
    }

}
