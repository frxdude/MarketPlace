package com.diplom.marketplace.serviceImpl;

import com.diplom.marketplace.repository.UserRepository;
import com.diplom.marketplace.service.AuthService;
import com.diplom.marketplace.service.UserService;
import com.diplom.marketplace.dto.request.auth.AuthRequest;
import com.diplom.marketplace.dto.request.auth.ConfirmForgotRequest;
import com.diplom.marketplace.dto.request.otp.ConfirmOTPRequest;
import com.diplom.marketplace.dto.request.otp.GenerateOTPRequest;
import com.diplom.marketplace.dto.response.auth.AuthResponse;
import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.helper.Localization;
import com.diplom.marketplace.jwt.JwtTokenProvider;
import com.diplom.marketplace.repository.UserRepository;
import com.diplom.marketplace.service.AuthService;
import com.diplom.marketplace.service.OTPService;
import com.diplom.marketplace.service.UserService;
import com.diplom.marketplace.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * AuthServiceImpl
 *
 * @author Sainjargal Ishdorj
 **/
@Service
public class AuthServiceImpl implements AuthService {

    UserService userService;
    UserRepository userRepository;
    AuthenticationManager authenticationManager;
    JwtTokenProvider jwtTokenProvider;
    OTPService otpService;
    PasswordEncoder encoder;
    Localization localization;

    @Autowired
    public AuthServiceImpl(UserService userService, UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, OTPService otpService, PasswordEncoder encoder, Localization localization) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.otpService = otpService;
        this.encoder = encoder;
        this.localization = localization;
    }

    /**
     * @param user     {@link User} entity
     * @param password user password
     * @return {@link AuthResponse}
     * @throws BusinessException thrown when username or password doesn't match
     * @author Sainjargal Ishdorj
     **/

    private AuthResponse authenticate(User user, String password) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[authenticate][input][User(id=" + user.getId() + ", email=" + user.getEmail() + ")]");
            if (!encoder.matches(password, user.getPassword()))
                throw new BusinessException(localization.getMessage("auth.username.pass.not.match"), "Password doesn't match");

            if (authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), password)).isAuthenticated()) {
                AuthResponse response = AuthResponse.builder()
                        .user(user)
                        .accessToken(jwtTokenProvider.createToken(user.getId(), user.getRoles().get(0)))
                        .build();
                Logger.info(getClass().getName(), "[authenticate][output][" + response.toString() + "]");
                return response;
            } else
                throw new BusinessException(localization.getMessage("auth.username.pass.not.match"), "username or password doesnt match");
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[authenticate][" + ex.reason + "]");
            throw ex;
        } catch (BadCredentialsException ex) {
            throw new BusinessException(localization.getMessage("auth.deactivated.user"), "deactivated user");
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[authenticate][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param authRequest {@link AuthRequest}
     * @return {@link AuthResponse}
     * @throws BusinessException thrown when username or password doesn't match
     * @author Sainjargal Ishdorj
     **/

    public AuthResponse login(AuthRequest authRequest, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[login][input][" + authRequest.toString() + "]");
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new BusinessException(localization.getMessage("user.not.found"), "User not found"));

            AuthResponse response = authenticate(user, authRequest.getPassword());

            Logger.info(getClass().getName(), "[login][output][" + response.toString() + "]");
            return response;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[login][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[login][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param email String
     * @throws BusinessException thrown when user not found or not exists
     * @author Sainjargal Ishdorj
     **/

    public void forgot(String email, HttpServletRequest req) throws BusinessException, UnsupportedEncodingException {
        try {
            Logger.info(getClass().getName(), "[forgot][input][email=" + email + "]");

            if (StringUtils.isBlank(email))
                throw new BusinessException(localization.getMessage("val.not.null"), "Email cannot be null");

            if (!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"))
                throw new BusinessException(localization.getMessage("val.email"), "Email format doesn't match");

            otpService.sendOtp(GenerateOTPRequest.builder().email(email).build(), true);
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[forgot][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[forgot][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param confirmForgotRequest {@link ConfirmForgotRequest}
     * @return {@link AuthResponse} entity
     * @throws BusinessException thrown when user not found or not exists
     * @author Sainjargal Ishdorj
     **/

    public AuthResponse confirmForgot(ConfirmForgotRequest confirmForgotRequest, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[confirmForgot][input][" + confirmForgotRequest.toString() + "]");

            User user = userRepository.findByEmail(confirmForgotRequest.getEmail())
                    .orElseThrow(() -> new BusinessException(localization.getMessage("user.not.found"), "User not found"));
            ConfirmOTPRequest confirmOTPRequest = new ConfirmOTPRequest();
            confirmOTPRequest.setOtp(confirmForgotRequest.getOneTimePassword());
            confirmOTPRequest.setEmail(confirmForgotRequest.getEmail());

            if (otpService.confirmOTP(confirmOTPRequest)) {
                user.setPassword(encoder.encode(confirmForgotRequest.getNewPassword()));
                userRepository.save(user);
            }

            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(jwtTokenProvider.createToken(confirmForgotRequest.getEmail(), user.getRoles().get(0)))
                    .user(user)
                    .build();

            Logger.info(getClass().getName(), "[confirmForgot][output][" + authResponse.toString() + "]");
            return authResponse;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[confirmForgot][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[confirmForgot][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }
}
