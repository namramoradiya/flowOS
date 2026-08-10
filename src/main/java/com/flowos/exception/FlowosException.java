package com.flowos.exception;

import org.springframework.http.HttpStatus;

public class FlowosException extends RuntimeException {

    private final HttpStatus status;
    public FlowosException(String message , HttpStatus status) {
        super(message);
        this.status=status;
    }
    public HttpStatus getStatus(){
        return status;
    }
}
