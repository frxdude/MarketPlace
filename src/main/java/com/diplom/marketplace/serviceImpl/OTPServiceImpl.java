package com.diplom.marketplace.serviceImpl;

import com.diplom.marketplace.helper.Localization;
import com.diplom.marketplace.jwt.JwtTokenProvider;
import com.diplom.marketplace.repository.UserRepository;
import com.diplom.marketplace.repository.redis.OTPRepository;
import com.diplom.marketplace.service.OTPService;
import com.diplom.marketplace.dto.request.otp.ConfirmOTPRequest;
import com.diplom.marketplace.dto.request.otp.GenerateOTPRequest;
import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.entity.redis.OneTimePassword;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.helper.MailThread;
import com.diplom.marketplace.util.Logger;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * OTPServiceImpl
 *
 * @author Sainjargal Ishdorj
 **/

@Service
public class OTPServiceImpl implements OTPService {

    Localization localization;
    UserRepository userRepository;
    OTPRepository otpRepository;
    JwtTokenProvider jwtTokenProvider;
    HashMap<String, String> response;

    @Autowired
    public OTPServiceImpl(Localization localization, UserRepository userRepository, OTPRepository otpRepository, JwtTokenProvider jwtTokenProvider) {
        this.localization = localization;
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * @param otpRequest {@link GenerateOTPRequest} DTO
     * @return HashMap response
     * @throws BusinessException thrown when user exists,
     * @author Sainjargal Ishdorj
     **/

    public HashMap<String, String> sendOtp(GenerateOTPRequest otpRequest, boolean forgot) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[sendOtp][input][" + otpRequest.toString() + "]");

            if (forgot) userRepository.findByEmail(otpRequest.getEmail())
                    .orElseThrow(() -> new BusinessException(localization.getMessage("user.not.found"), "User not found"));

            Optional<User> optionalUser = userRepository.findByEmail(otpRequest.getEmail());

            if (!forgot && optionalUser.isPresent() && optionalUser.get().isActive())
                throw new BusinessException(localization.getMessage("user.already"), "User already exists");

            String generatedOTP = RandomStringUtils.randomNumeric(4);
            response = new HashMap<>();
            Optional<OneTimePassword> optionalOTP = otpRepository.findById(otpRequest.getEmail());

            if (optionalOTP.isPresent()) {
                OneTimePassword otp = optionalOTP.get();
                throw new BusinessException(localization.getMessage("otp.time", new Object[]{otp.getTimeout()}), "Confirmation password is alive");
            } else {
                MailThread mailThread = new MailThread("<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\n" +
                        "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\n" +
                        "    <div style=\"border-bottom:1px solid #eee\">\n" +
                        "      <a href=\"https://nubisoft.mn\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">Nubisoft</a>\n" +
                        "    </div>\n" +
                        "    <p style=\"font-size:1.1em\">Hi,</p>\n" +
                        "    <p>Баталгаажуулах кодын хүчингүй болох хугацаа 5 минут</p>\n" +
                        "    <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + generatedOTP + "</h2>\n" +
                        "    <p style=\"font-size:0.9em;\">Хүндэтгэсэн,<br />Nubisoft</p>\n" +
                        "    <hr style=\"border:none;border-top:1px solid #eee\" />\n" +
                        "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\n" +
                        "      <p>Nubisoft LLC</p>\n" +
                        "      <p>Max Tower</p>\n" +
                        "      <p>Улаанбаатар хот</p>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>", otpRequest.getEmail(), "Payment Gateway");
                mailThread.start();

                otpRepository.save(new OneTimePassword(otpRequest.getEmail(), generatedOTP, 3, 300L));
                response.put("message", localization.getMessage("otp.send", new Object[]{otpRequest.getEmail(), generatedOTP}));
            }
            Logger.info(getClass().getName(), "[sendOtp][output][" + response.get("message") + "]");
            return response;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[sendOtp][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[sendOtp][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param otpRequest {@link ConfirmOTPRequest} DTO
     * @return boolean
     * @throws BusinessException thrown when otp expired, tries exceeded, doesn't match
     * @author Sainjargal Ishdorj
     **/

    public boolean confirmOTP(ConfirmOTPRequest otpRequest) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[confirmOTP][input][" + otpRequest.toString() + "]");

            OneTimePassword oneTimePassword = otpRepository.findById(otpRequest.getEmail())
                    .orElseThrow(() -> new BusinessException(localization.getMessage("otp.expired"), "Confirm time expired"));

            if (oneTimePassword.getTries() <= 0)
                throw new BusinessException(localization.getMessage("otp.tries.exceeded"), "Tries exceeded");

            if (!oneTimePassword.getOtp().equals(otpRequest.getOtp())) {
                oneTimePassword.setTries(oneTimePassword.getTries() - 1);
                otpRepository.save(oneTimePassword);
                throw new BusinessException(localization.getMessage("otp.invalid"), "Invalid confirm password");
            }
            otpRepository.delete(oneTimePassword);
            Logger.info(getClass().getName(), "[confirmOTP][output][]");
            return true;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[confirmOTP][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[confirmOTP][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }
}
