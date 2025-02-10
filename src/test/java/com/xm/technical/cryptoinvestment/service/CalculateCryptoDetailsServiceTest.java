package com.xm.technical.cryptoinvestment.service;

import com.xm.technical.cryptoinvestment.model.Crypto;
import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class CalculateCryptoDetailsServiceTest {

    @InjectMocks
    private CalculateCryptoDetailsService calculateCryptoDetailsService;

    @Test
    public void calculateValuesListTest(){
        List<CryptoDetails> cryptoDetailsList = calculateCryptoDetailsService.calculateDetailsList(buildCryptoData());

        assertThat(cryptoDetailsList.get(0).getCryptoName().equals("BTC"));
        assertThat(cryptoDetailsList.get(0).getNewest().compareTo(BigDecimal.valueOf(45555.33))==0);
        assertThat(cryptoDetailsList.get(0).getOldest().compareTo(BigDecimal.valueOf(45664.17))==0);
        assertThat(cryptoDetailsList.get(0).getMin().compareTo(BigDecimal.valueOf(45664.17))==0);
        assertThat(cryptoDetailsList.get(0).getMax().compareTo(BigDecimal.valueOf(45664.17))==0);

        assertThat(cryptoDetailsList.get(1).getCryptoName().equals("ETH"));
        assertThat(cryptoDetailsList.get(1).getNewest().compareTo(BigDecimal.valueOf(45664.17))==0);
        assertThat(cryptoDetailsList.get(1).getOldest().compareTo(BigDecimal.valueOf(45664.17))==0);
        assertThat(cryptoDetailsList.get(1).getMin().compareTo(BigDecimal.valueOf(45664.17))==0);
        assertThat(cryptoDetailsList.get(1).getMax().compareTo(BigDecimal.valueOf(45664.17))==0);
    }

    private Map<String, List<Crypto>> buildCryptoData(){
        Map<String, List<Crypto>> cryptoData = new HashMap<>();
        List<Crypto> btcList = new ArrayList<>();
        List<Crypto> ethList = new ArrayList<>();
        btcList.add(new Crypto("1641106500000","BTC", BigDecimal.valueOf(45555.33)));
        btcList.add(new Crypto("1641106600000", "BTC", BigDecimal.valueOf(45664.17)));
        btcList.add(new Crypto("1641106700000","BTC", BigDecimal.valueOf(43000.33)));
        btcList.add(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(46000.17)));
        ethList.add(new Crypto("1641106500000", "BTC", BigDecimal.valueOf(35333.12)));
        ethList.add(new Crypto("1641106600000", "BTC", BigDecimal.valueOf(35123.17)));
        ethList.add(new Crypto("1641106700000", "BTC", BigDecimal.valueOf(34333.12)));
        ethList.add(new Crypto("1641106800000", "BTC", BigDecimal.valueOf(36123.17)));
        cryptoData.put("BTC", btcList);
        cryptoData.put("ETH", ethList);
        return cryptoData;
    }

}
