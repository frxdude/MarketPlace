package com.diplom.marketplace.exception;


import com.diplom.marketplace.model.ErrorDetail;

import java.util.List;

public class BusinessException extends Exception {

    public List<ErrorDetail> errorDetails;

    public String reason;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

}
