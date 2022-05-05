package com.diplom.marketplace.dto.request.otp;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * ConfirmOTPRequest
 *
 * @author Sainjargal Ishdorj
 **/

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ConfirmOTPRequest extends GenerateOTPRequest {

    @NotNull(message = "{val.not.null}")
    @Length(min = 6, max = 6, message = "{val.length}")
    private String otp;

}
