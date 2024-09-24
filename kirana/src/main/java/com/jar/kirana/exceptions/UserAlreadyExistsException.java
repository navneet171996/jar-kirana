package com.jar.kirana.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
