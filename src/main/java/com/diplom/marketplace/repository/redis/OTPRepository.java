package com.diplom.marketplace.repository.redis;

import com.diplom.marketplace.entity.redis.OneTimePassword;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * OTPRepository
 *
 * @author Sainjargal Ishdorj
 **/

public interface OTPRepository extends CrudRepository<OneTimePassword, String> {

    Optional<OneTimePassword> findById(String type, String id);

}