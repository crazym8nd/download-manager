package com.vitaly.dlmanager.exception;
//  17-Feb-24
// gh crazym8nd


public class AuthException extends ApiException {
    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
