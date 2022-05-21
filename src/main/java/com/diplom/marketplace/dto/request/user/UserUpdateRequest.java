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

    @NotNull(message = "{val.not.null}")
    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Length(min = 2, max = 64, message = "{val.length}")
    private String lastname;

    @NotNull(message = "{val.not.null}")
    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
    @Length(min = 2, max = 64, message = "{val.length}")
    private String firstname;

    @NotNull(message = "{val.not.null}")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "{val.email}")
    private String email;

    @NotBlank(message = "{val.not.null}")
    @Length(min = 8, max = 8, message = "{val.length}")
    @Pattern.List({
            @Pattern(regexp = "^85.*|^94.*|^95.*|^99.*|^90.*|^91.*|^96.*|^80.*|^86.*|^88.*|^89.*|^83.*|^93.*|^97.*|^98.*", message = "{val.phone}"),
            @Pattern(regexp = "\\d+", message = "{val.number}")
    })
    private String phone;

}
