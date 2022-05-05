package com.diplom.marketplace.controller;

import com.diplom.marketplace.dto.request.user.UserRegisterRequest;
import com.diplom.marketplace.dto.response.ErrorResponse;
import com.diplom.marketplace.entity.User;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

/**
 * UserController
 *
 * @author Sainjargal Ishdorj
 **/

@Api(tags = "User")
@RestController
@RequestMapping("users")
public class UserController {

    UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @ApiOperation(value = "Хэрэглэгчдийн мэдээлэл татах. | ROLE_ADMIN", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = Page.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Object> findAllUser(@RequestParam String searchPattern,
                                               @RequestParam int page,
                                               @RequestParam int size,
                                               HttpServletRequest req) {
        return ResponseEntity.ok(service.findAllUser(searchPattern, page, size, req));
    }

    @ApiOperation(value = "Бүртгэх. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Body-гүй CREATED статус буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest registerRequest, HttpServletRequest req) throws BusinessException {
        service.register(registerRequest, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
