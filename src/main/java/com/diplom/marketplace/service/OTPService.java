package com.diplom.marketplace.service;

import com.diplom.marketplace.dto.request.otp.ConfirmOTPRequest;
import com.diplom.marketplace.dto.request.otp.GenerateOTPRequest;
import com.diplom.marketplace.exception.BusinessException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * OTPService
 *
 * @author Sainjargal Ishdorj
 **/

public interface OTPService {

    HashMap<String, String> sendOtp(GenerateOTPRequest otpRequest, boolean forgot) throws BusinessException, UnsupportedEncodingException;

    boolean confirmOTP(ConfirmOTPRequest otpRequest) throws BusinessException;
}
