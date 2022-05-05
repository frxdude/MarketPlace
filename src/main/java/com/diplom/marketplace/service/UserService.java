package com.diplom.marketplace.service;

import com.diplom.marketplace.dto.request.user.UserRegisterRequest;
import com.diplom.marketplace.entity.enums.Role;
import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.exception.BusinessException;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

/**
 * UserService
 *
 * @author Sainjargal Ishdorj
 **/

public interface UserService {

    Page<User> findAll(String searchPattern, int page, int size, Role role, HttpServletRequest req);

    Page<User> findAllUser(String searchPattern, int page, int size, HttpServletRequest req);

    Page<User> findAllAdmin(String searchPattern, int page, int size, HttpServletRequest req);

    User findById(String id) throws BusinessException;

    void register(UserRegisterRequest registerRequest, HttpServletRequest req) throws BusinessException;

}
