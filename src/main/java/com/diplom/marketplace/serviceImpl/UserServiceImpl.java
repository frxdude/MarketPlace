package com.diplom.marketplace.serviceImpl;

import com.diplom.marketplace.dto.request.user.UserRegisterRequest;
import com.diplom.marketplace.dto.request.user.UserUpdateRequest;
import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.entity.enums.Role;
import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.helper.Localization;
import com.diplom.marketplace.helper.specification.SearchCriteria;
import com.diplom.marketplace.helper.specification.PostSpecification;
import com.diplom.marketplace.helper.specification.UserSpecification;
import com.diplom.marketplace.repository.PostRepository;
import com.diplom.marketplace.repository.UserRepository;
import com.diplom.marketplace.service.UserService;
import com.diplom.marketplace.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

/**
 * UserServiceImpl
 *
 * @author Sainjargal Ishdorj
 **/

@Service
public class UserServiceImpl implements UserService {

    UserRepository repository;
    PostRepository postRepository;
    Localization localization;
    PasswordEncoder encoder;
    UserSpecification firstnameSpec, lastnameSpec, emailSpec, phoneSpec, roleSpec;
    PostSpecification titleSpec, userSpec;

    @Autowired
    public UserServiceImpl(UserRepository repository, PostRepository postRepository, Localization localization, PasswordEncoder encoder, UserSpecification firstnameSpec, UserSpecification lastnameSpec, UserSpecification emailSpec, UserSpecification phoneSpec, UserSpecification roleSpec, PostSpecification titleSpec, PostSpecification userSpec) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.localization = localization;
        this.encoder = encoder;
        this.firstnameSpec = firstnameSpec;
        this.lastnameSpec = lastnameSpec;
        this.emailSpec = emailSpec;
        this.phoneSpec = phoneSpec;
        this.roleSpec = roleSpec;
        this.titleSpec = titleSpec;
        this.userSpec = userSpec;
    }

    /**
     * @param searchPattern String
     * @param page          int
     * @param size          int
     * @return Page of {@link User}
     * @author Sainjargal Ishdorj
     **/

    public Page<User> findAll(String searchPattern, int page, int size, HttpServletRequest req) {
        try {
            Logger.info(getClass().getName(), "[findAll][input][searchPattern=" + searchPattern + ", page=" + page + ", size= " + size + "]");

            emailSpec = StringUtils.isNotBlank(searchPattern)
                    ? new UserSpecification(new SearchCriteria("email", ".%", searchPattern))
                    : new UserSpecification();

            firstnameSpec = StringUtils.isNotBlank(searchPattern)
                    ? new UserSpecification(new SearchCriteria("firstname", ".%", searchPattern))
                    : new UserSpecification();

            lastnameSpec = StringUtils.isNotBlank(searchPattern)
                    ? new UserSpecification(new SearchCriteria("lastname", ".%", searchPattern))
                    : new UserSpecification();

            phoneSpec = StringUtils.isNotBlank(searchPattern)
                    ? new UserSpecification(new SearchCriteria("phone", ".%", searchPattern))
                    : new UserSpecification();

            Page<User> userPage = repository.findAll(firstnameSpec.or(lastnameSpec.or(phoneSpec.or(emailSpec))),
                    PageRequest.of(page, size, Sort.by("id").descending()));

            Logger.info(getClass().getName(), "[findAll][output][totalElements=" + userPage.getTotalElements() +", size=" + userPage.getSize() + "]");
            return userPage;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[findAll][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param id String
     * @return {@link User}
     * @throws BusinessException thrown when user not found
     * @author Sainjargal Ishdorj
     **/

    public User findById(String id) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[findById][input][id=" + id + "]");
            User user = repository.findById(id)
                    .orElseThrow(() -> new BusinessException(localization.getMessage("user.not.found"), "User not found"));
            Logger.info(getClass().getName(), "[findById][output][User(id=" + user.getId() + ")]");
            return user;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[findById][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[findById][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param registerRequest {@link UserRegisterRequest}
     * @param req             servletRequest
     * @throws BusinessException thrown when user already exists
     * @author Sainjargal Ishdorj
     **/

    public void register(UserRegisterRequest registerRequest, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[register][input][" + registerRequest.toString() + "]");
            Optional<User> optionalUser = repository.findByEmail(registerRequest.getEmail());
            if (optionalUser.isPresent())
                throw new BusinessException(localization.getMessage("user.already"), "User already exists");

            repository.save(User.builder()
                    .email(registerRequest.getEmail())
                    .password(encoder.encode(registerRequest.getPassword()))
                    .roles(Collections.singletonList(Role.ROLE_USER))
                    .phone(registerRequest.getPhone())
                    .isActive(true)
                    .build());

            Logger.info(getClass().getName(), "[register][output][]");
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[register][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[register][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param updateRequest {@link UserUpdateRequest}
     * @param req           servletRequest
     * @throws BusinessException thrown when user not found
     * @author Sainjargal Ishdorj
     **/

    public User update(UserUpdateRequest updateRequest, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[update][input][" + updateRequest.toString() + "]");
            User user = findById(req.getRemoteUser());
            user.setEmail(updateRequest.getEmail());
            user.setFirstname(updateRequest.getFirstname());
            user.setLastname(updateRequest.getLastname());
            user.setPhone(updateRequest.getPhone());
            repository.save(user);
            Logger.info(getClass().getName(), "[update][output][User(email=" + user.getEmail() + ", firstname=" + user.getFirstname() + ", lastname=" + user.getLastname() + ")]");
            return user;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[update][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[update][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param id            String
     * @param searchPattern String
     * @param page          int
     * @param size          int
     * @param req           servletRequest
     * @author Sainjargal Ishdorj
     **/

    public Page<Post> findPostsByUser(String id, String searchPattern, int page, int size, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[findPostsByUser][input][id=" + id + ", searchPattern=" + searchPattern + ", page=" + page + ", size=" + size + "]");
            User user = findById(id);

            titleSpec = StringUtils.isNotBlank(searchPattern)
                    ? new PostSpecification(new SearchCriteria("title", ".%", searchPattern))
                    : new PostSpecification();

            userSpec = StringUtils.isNotBlank(id)
                    ? new PostSpecification(new SearchCriteria("users", "<<", id))
                    : new PostSpecification();

            Page<Post> postPage = postRepository.findAll(titleSpec.and(userSpec), PageRequest.of(page, size));

            Logger.info(getClass().getName(), "[findPostsByUser][output][User(email=" + user.getEmail() + ", firstname=" + user.getFirstname() + ", lastname=" + user.getLastname() + ")]");
            return postPage;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[findPostsByUser][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[findPostsByUser][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }


    /**
     * @param id  String
     * @param req servletRequest
     * @author Sainjargal Ishdorj
     **/

    public void delete(String id, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[delete][input][id=" + id + "]");
            User user = findById(id);
            repository.delete(user);
            Logger.info(getClass().getName(), "[delete][output][]");
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[delete][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[delete][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }
}
