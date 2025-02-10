package com.xm.technical.cryptoinvestment.controller;

import com.xm.technical.cryptoinvestment.model.CryptoDetails;
import com.xm.technical.cryptoinvestment.model.CryptoNormalizedRange;
import com.xm.technical.cryptoinvestment.model.DateRequestDTO;
import com.xm.technical.cryptoinvestment.service.CryptoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CryptoController implements CryptoControllerInterface{

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService){
        this.cryptoService = cryptoService;
    }

    public List<CryptoNormalizedRange> cryptoNormalizedRangeDetails(){
        return cryptoService.getSortedNormalizedRange();
    }

    public CryptoDetails cryptoDetails(@PathVariable String cryptoName){
        return cryptoService.calculateCryptoDetails(cryptoName);
    }

    public CryptoNormalizedRange highestNormalizedThisDay(@RequestBody DateRequestDTO date){
        return cryptoService.getHighestNormalizedRangeForThisDay(date.getDate());
    }

}
