package com.diplom.marketplace.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

/**
 * OneTimePassword
 *
 * @author Sainjargal Ishdorj
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("OTP")
public class OneTimePassword {

    @Id
    private String id;

    private String otp;

    private int tries;

    //Default is seconds
    @TimeToLive
    private Long timeout;

}
