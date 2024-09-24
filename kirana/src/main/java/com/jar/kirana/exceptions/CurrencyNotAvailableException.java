package com.jar.kirana.exceptions;

public class CurrencyNotAvailableException extends RuntimeException{
    public CurrencyNotAvailableException(String msg){
        super(msg);
    }
}
