package com.diplom.marketplace.service;

import com.diplom.marketplace.dto.request.user.UserRegisterRequest;
import com.diplom.marketplace.dto.request.user.UserUpdateRequest;
import com.diplom.marketplace.entity.Post;
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

    Page<User> findAll(String searchPattern, int page, int size, HttpServletRequest req);

    User findById(String id) throws BusinessException;

    Page<Post> findPostsByUser(String id, String searchPattern, int page, int size, HttpServletRequest req) throws BusinessException;

    void register(UserRegisterRequest registerRequest, HttpServletRequest req) throws BusinessException;

    User update(UserUpdateRequest updateRequest, HttpServletRequest req) throws BusinessException;

    void delete(String id, HttpServletRequest req) throws BusinessException;

}
