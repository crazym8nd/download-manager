package com.vitaly.dlmanager.exception;
//  17-Feb-24
// gh crazym8nd

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ApiException{
    public UnauthorizedException(String message) {
        super(message,"UNAUTHORIZED");
    }
}
