package com.diplom.marketplace.exception;

import com.diplom.marketplace.model.ErrorDetail;

import java.util.List;

/**
 * RMIException
 *
 * @author tushig
 **/

public class RMIException extends Exception {

    public List<ErrorDetail> errorDetails;

    public String reason;

    public RMIException() {
    }

    public RMIException(String message) {
        super(message);
    }

    public RMIException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

}
