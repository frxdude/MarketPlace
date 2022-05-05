package com.diplom.marketplace.dto.request.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * UserUpdateRequest
 *
 * @author Sainjargal Ishdorj
 **/

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserUpdateRequest {

    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Length(min = 2, max = 64, message = "{val.length}")
    private String lastname;

    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Length(min = 2, max = 64, message = "{val.length}")
    private String firstname;

    @NotNull(message = "{val.not.null}")
    @Pattern(regexp = "^(.+)@(\\\\S+)$", message = "{val.email}")
    private String email;

}
