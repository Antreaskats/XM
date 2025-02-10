package com.xm.technical.cryptoinvestment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CryptoExceptions {

    @ExceptionHandler(CryptoNotSupportedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleCryptoNotSupportedException(CryptoNotSupportedException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(CryptoNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleCryptoNotFoundThisDayException(CryptoNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

}
