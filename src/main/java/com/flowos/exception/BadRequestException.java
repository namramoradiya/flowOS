package com.flowos.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends FlowosException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}