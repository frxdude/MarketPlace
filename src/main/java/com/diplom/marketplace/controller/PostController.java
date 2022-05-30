package com.diplom.marketplace.controller;

import com.diplom.marketplace.dto.request.post.PostAddRequest;
import com.diplom.marketplace.dto.request.post.PostUpdateRequest;
import com.diplom.marketplace.dto.request.user.UserRegisterRequest;
import com.diplom.marketplace.dto.response.ErrorResponse;
import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.service.PostService;
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

/**
 * PostController
 *
 * @author Sainjargal Ishdorj
 **/

@Api(tags = "Post")
@RestController
@RequestMapping("posts")
public class PostController {

    PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @ApiOperation(value = "Мэдээлэл татах. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Page.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> find(@PathVariable String id, HttpServletRequest req) throws BusinessException {
        return ResponseEntity.ok(service.find(id, req));
    }

    @ApiOperation(value = "Мэдээллүүд татах. | ", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Page.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> findAll(@RequestParam String searchPattern,
                                              @RequestParam int page,
                                              @RequestParam int size,
                                              @RequestParam(required = false, defaultValue = "0") long minPrice,
                                              @RequestParam(required = false, defaultValue = "0") long maxPrice,
                                              @RequestParam(required = false, defaultValue = "0") float minArea,
                                              @RequestParam(required = false, defaultValue = "0") float maxArea,
                                              HttpServletRequest req) {
        return ResponseEntity.ok(service.findAll(searchPattern, page, size, minPrice, maxPrice, minArea, maxArea, req));
    }

    @ApiOperation(value = "Мэдээ нэмэх. | ROLE_USER", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = Page.class, message = "Body-гүй Created статус буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> add(@Valid @RequestBody PostAddRequest addRequest, HttpServletRequest req) throws BusinessException {
        service.add(addRequest, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Мэдээний мэдээлэл засах. | ROLE_USER", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, response = Post.class, message = "{} Object буцна"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> update(@PathVariable String id, @Valid @RequestBody PostUpdateRequest updateRequest, HttpServletRequest req) throws BusinessException {
        return ResponseEntity.ok(service.update(id, updateRequest, req));
    }

    @ApiOperation(value = "Зар устгах. | ROLE_USER", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Body-гүй NoContent статус буцнаө"),
            @ApiResponse(code = 400, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 401, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 403, response = ErrorResponse.class, message = "{} Object буцна"),
            @ApiResponse(code = 500, response = ErrorResponse.class, message = "{} Object буцна"),
    })
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable String id,
                                         HttpServletRequest req) throws BusinessException {
        service.delete(id, req);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
