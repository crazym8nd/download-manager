package com.vitaly.dlmanager.exception;
//  17-Feb-24
// gh crazym8nd


public class ApiException extends RuntimeException{
    protected String errorCode;

    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
