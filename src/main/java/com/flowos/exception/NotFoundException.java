package com.flowos.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends FlowosException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}