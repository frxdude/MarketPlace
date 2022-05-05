package com.diplom.marketplace.exception;

import com.diplom.marketplace.model.ErrorDetail;

import java.util.List;

public class ValidationException extends Exception {

    public List<ErrorDetail> errorDetails;

    public String reason;

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

}
