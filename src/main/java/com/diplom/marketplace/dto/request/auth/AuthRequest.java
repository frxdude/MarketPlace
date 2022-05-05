package com.diplom.marketplace.dto.request.auth;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * AuthRequest
 *
 * @author Sainjargal Ishdorj
 **/

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class AuthRequest {

    @NotNull(message = "{val.not.null}")
    @Length(min = 4, message = "{val.length}")
    private String email;

    @NotNull(message = "{val.not.null}")
    @Length(min = 4, max = 30, message = "{val.length}")
    private String password;

}
