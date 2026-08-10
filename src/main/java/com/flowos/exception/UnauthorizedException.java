package com.flowos.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends FlowosException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
