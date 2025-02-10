package com.xm.technical.cryptoinvestment.service;

import com.xm.technical.cryptoinvestment.exception.CryptoNotFoundException;
import com.xm.technical.cryptoinvestment.exception.CryptoNotSupportedException;
import com.xm.technical.cryptoinvestment.model.Crypto;
import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import com.xm.technical.cryptoinvestment.model.CryptoNormalizedRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CryptoServiceTest {

    @InjectMocks
    private CryptoService cryptoService;

    @Mock
    private PriceReaderService priceReaderService;
    @Mock
    private CalculateCryptoDetailsService calculateCryptoDetailsService;


    @BeforeEach
    public void setup(){
        when(priceReaderService.getCryptoData()).thenReturn(buildCryptoData());
    }

    @Test
    public void getSortedNormalizedRangeTest(){
        when(calculateCryptoDetailsService.calculateDetailsList(anyMap())).thenReturn(buildCryptoDetails());
        List<CryptoNormalizedRange> normalizedRangeList = cryptoService.getSortedNormalizedRange();
        assertThat(normalizedRangeList.size()==2);
        assertThat(normalizedRangeList.get(0).getNormalizedRange().compareTo(normalizedRangeList.get(1).getNormalizedRange())==1);
    }

    @Test
    public void calculateCryptoValuesTest(){
        when(calculateCryptoDetailsService.calculateDetailsList(anyMap())).thenReturn(List.of(cryptoDetail));
        CryptoDetails cryptoDetails = cryptoService.calculateCryptoDetails("BTC");
        assertThat(cryptoDetails.getCryptoName().equals("BTC"));
        assertThat(cryptoDetails.getMin().compareTo(BigDecimal.valueOf(43000.13))==0);
        assertThat(cryptoDetails.getMax().compareTo(BigDecimal.valueOf(46000.13))==0);
        assertThat(cryptoDetails.getOldest().compareTo(BigDecimal.valueOf(45120.13))==0);
        assertThat(cryptoDetails.getNewest().compareTo(BigDecimal.valueOf(45032.13))==0);
    }

    @Test
    public void getHighestNormalizedRangeForThisDayTest(){
        Map<String, List<Crypto>> cryptoMap = new HashMap<>();
        cryptoMap.put("BTC", List.of(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(10))));
        cryptoMap.put("ETH", List.of(new Crypto("1641106800000", "ETH", BigDecimal.valueOf(5))));
        List<CryptoDetails> cryptoDetailsList = List.of(new CryptoDetails("BTC", BigDecimal.valueOf(5), BigDecimal.valueOf(4), BigDecimal.valueOf(1), BigDecimal.valueOf(3)),
                new CryptoDetails("ETH", BigDecimal.valueOf(3), BigDecimal.valueOf(3), BigDecimal.valueOf(3), BigDecimal.valueOf(4)));
        when(priceReaderService.getCryptoData()).thenReturn(cryptoMap);
        when(calculateCryptoDetailsService.calculateDetailsList(anyMap())).thenReturn(cryptoDetailsList);
        CryptoNormalizedRange normalizedRange = cryptoService.getHighestNormalizedRangeForThisDay(LocalDate.parse("2022-01-02"));
        assertThat(normalizedRange.getNormalizedRange().compareTo(BigDecimal.valueOf(9))==0);
        assertThat(normalizedRange.getName().equals("BTC"));

    }

    @Test
    public void getHighestNormalizedRangeForThisDayExceptionTest(){
        when(priceReaderService.getCryptoData()).thenReturn(Collections.emptyMap());
        when(calculateCryptoDetailsService.calculateDetailsList(anyMap())).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(CryptoNotFoundException.class, () -> cryptoService.getHighestNormalizedRangeForThisDay(LocalDate.parse("2025-02-07")));
        assertEquals("No crypto normalized range found for this day", exception.getMessage());
    }

    @Test
    public void calculateCryptoValuesExceptionTest(){
        when(calculateCryptoDetailsService.calculateDetailsList(anyMap())).thenReturn(List.of(cryptoDetail));
        Exception exception = assertThrows(CryptoNotSupportedException.class, () -> cryptoService.calculateCryptoDetails("ETH"));
        assertEquals("ETH crypto is not yet supported yet", exception.getMessage());
    }

    @Test
    public void getSortedNormalizedRangeExceptionTest(){
        when(priceReaderService.getCryptoData()).thenReturn(Collections.emptyMap());
        Exception exception = assertThrows(CryptoNotFoundException.class, () -> cryptoService.getSortedNormalizedRange());
        assertEquals("No cryptos found", exception.getMessage());
    }



    private Map<String, List<Crypto>> buildCryptoData(){
        Map<String, List<Crypto>> cryptoData = new HashMap<>();
        cryptoData.put("BTC", List.of(new Crypto("1641106800000","BTC", BigDecimal.valueOf(45000.33))));
        cryptoData.put("BTC", List.of(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(46000.17))));
        cryptoData.put("BTC", List.of(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(45333.12))));
        cryptoData.put("BTC", List.of(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(45123.17))));
        return cryptoData;
    }

    private List<CryptoDetails> buildCryptoDetails(){
        List<CryptoDetails> cryptoDetailsList = new ArrayList<>();
        cryptoDetailsList.add(new CryptoDetails("BTC", BigDecimal.valueOf(45000.33), BigDecimal.valueOf(35123.13), BigDecimal.valueOf(33412.12), BigDecimal.valueOf(41213.65)));
        cryptoDetailsList.add(new CryptoDetails("ETH", BigDecimal.valueOf(35000.33), BigDecimal.valueOf(25123.13), BigDecimal.valueOf(23412.12), BigDecimal.valueOf(31213.65)));
        return cryptoDetailsList;
    }

    private CryptoDetails cryptoDetail = new CryptoDetails("BTC", BigDecimal.valueOf(45120.13), BigDecimal.valueOf(45032.13), BigDecimal.valueOf(43000.13), BigDecimal.valueOf(46000.13));

}
