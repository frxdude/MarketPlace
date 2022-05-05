package com.diplom.marketplace.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends RunTimeException {

    public String message;

    public HttpStatus status;

    public TokenException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

}
