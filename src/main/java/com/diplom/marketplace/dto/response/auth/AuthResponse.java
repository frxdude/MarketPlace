package com.diplom.marketplace.dto.response.auth;

import com.diplom.marketplace.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * AuthResponse
 *
 * @author Sainjargal Ishdorj
 **/

@Builder
@ToString
@Getter
@Setter
public class AuthResponse {

    private User user;

    private String accessToken;

}
