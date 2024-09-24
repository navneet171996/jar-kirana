package com.jar.kirana.exceptions;

public class AuthenticationFailedException extends RuntimeException{

    public AuthenticationFailedException(String msg){
        super(msg);
    }
}
