package com.diplom.marketplace.service;

import com.diplom.marketplace.dto.request.auth.AuthRequest;
import com.diplom.marketplace.dto.request.auth.ConfirmForgotRequest;
import com.diplom.marketplace.dto.response.auth.AuthResponse;
import com.diplom.marketplace.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * AuthService
 *
 * @author Sainjargal Ishdorj
 **/

public interface AuthService {

    AuthResponse login(AuthRequest authRequest, HttpServletRequest req) throws BusinessException;

    void forgot(String email, HttpServletRequest req) throws BusinessException, UnsupportedEncodingException;

    AuthResponse confirmForgot(ConfirmForgotRequest confirmForgotRequest, HttpServletRequest req) throws BusinessException;

}