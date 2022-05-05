package com.diplom.marketplace.dto.request.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

/**
 * UserRegisterRequest
 *
 * @author Sainjargal Ishdorj
 **/

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterRequest {

//    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
//    @Length(min = 2, max = 64, message = "{val.length}")
//    private String lastname;
//
//    @Pattern(regexp = "([A-Za-zА-Яа-яөүӨҮёЁ. -]+)", message = "{val.letters}")
//    @Length(min = 2, max = 64, message = "{val.length}")
//    private String firstname;

    @NotNull(message = "{val.not.null}")
    private String email;

    @NotBlank(message = "{val.not.null}")
    @Length(min = 8, max = 8, message = "{val.length}")
    @Pattern.List({
            @Pattern(regexp = "^85.*|^94.*|^95.*|^99.*|^90.*|^91.*|^96.*|^80.*|^86.*|^88.*|^89.*|^83.*|^93.*|^97.*|^98.*", message = "{val.phone}"),
            @Pattern(regexp = "\\d+", message = "{val.number}")
    })
    private String phone;

//    @Pattern(regexp = "^(email|phone)$", message = "{val.type}")
//    @NotBlank(message = "{val.not.null}")
//    private String provider;

    @NotNull(message = "{val.not.null}")
    @Length(min = 4, max = 30, message = "{val.length}")
    private String password;

//    @Max(value = 500, message = "{val.max}")
//    @NotNull(message = "{val.not.null}")
//    private Integer weight;
//
//    @Max(value = 250, message = "{val.max}")
//    @NotNull(message = "{val.not.null}")
//    private Integer height;
//
//    @Max(value = 150, message = "{val.max}")
//    @Min(value = 0, message = "{val.min}")
//    @NotNull(message = "{val.not.null}")
//    private Integer age;

}
