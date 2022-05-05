package com.diplom.marketplace.controller;

import com.diplom.marketplace.dto.request.auth.AuthRequest;
import com.diplom.marketplace.dto.request.auth.ConfirmForgotRequest;
import com.diplom.marketplace.dto.request.otp.GenerateOTPRequest;
import com.diplom.marketplace.dto.response.ErrorResponse;
import com.diplom.marketplace.dto.response.auth.AuthResponse;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.service.AuthService;
import com.diplom.marketplace.service.OTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

/**
 * AuthController
 *
 * @author Sainjargal Ishdorj
 **/

@Api(tags = "Auth")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("auth")
public class AuthController {

    AuthService service;
    OTPService otpService;

    @Autowired
    public AuthController(AuthService service, OTPService otpService) {
        this.service = service;
        this.otpService = otpService;
    }

    @ApiOperation(value = "Нэвтрэх. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AuthResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<Object> login(@Valid @RequestBody AuthRequest authRequest, HttpServletRequest req) throws BusinessException {
        return ResponseEntity.ok(service.login(authRequest, req));
    }

    @ApiOperation(value = "Нууц үгээ сэргээх хүсэлт илгээх. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 204, response = AuthResponse.class, message = "Body-гүй noContent status буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "forgot", method = RequestMethod.GET)
    public ResponseEntity<Object> forgot(@RequestParam("email") String email,
                                               HttpServletRequest req) throws BusinessException, UnsupportedEncodingException {
        service.forgot(email, req);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Нууц үгээ сэргээх хүсэлт илгээх. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 204, response = AuthResponse.class, message = "Body-гүй noContent status буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "/otp", method = RequestMethod.GET)
    public ResponseEntity<Object> sendOtp(@RequestParam("email") String email,
                                         HttpServletRequest req) throws BusinessException, UnsupportedEncodingException {
        otpService.sendOtp(GenerateOTPRequest.builder().email(email).build(), false);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Баталгаажуулах кодоор нууц үгээ сэргээх. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = AuthResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "forgot/confirm", method = RequestMethod.POST)
    public ResponseEntity<Object> confirmForgot(@Valid @RequestBody ConfirmForgotRequest confirmForgotRequest, HttpServletRequest req) throws BusinessException {
        return ResponseEntity.ok(service.confirmForgot(confirmForgotRequest, req));
    }

//    @ApiOperation(value = "Баталгаажуулах код авах. | ", notes = "")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, response = HashMap.class, message = "{} Object буцна"),
//            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
//    })
//    @RequestMapping(value = "otp/send", method = RequestMethod.POST)
//    public ResponseEntity<Object> sendOTP(@Valid @RequestBody GenerateOTPRequest otpRequest,
//                                          HttpServletRequest req) throws BusinessException, UnsupportedEncodingException {
//        return ResponseEntity.ok(service.sendOtp(otpRequest, req));
//    }
//
//    @ApiOperation(value = "Баталгаажуулах код шалгагдах. | ", notes = "")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, response = ConfirmOTPResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
//            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
//    })
//    @RequestMapping(value = "otp/confirm", method = RequestMethod.POST)
//    public ResponseEntity<Object> confirmOTP(@Valid @RequestBody ConfirmOTPRequest otpRequest, HttpServletRequest req) throws BusinessException {
//        return ResponseEntity.ok(service.confirmOTP(otpRequest, req));
//    }
}
