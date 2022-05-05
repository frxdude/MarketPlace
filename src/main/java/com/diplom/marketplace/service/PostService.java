package com.diplom.marketplace.service;

import com.diplom.marketplace.dto.request.post.PostAddRequest;
import com.diplom.marketplace.dto.request.post.PostUpdateRequest;
import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.exception.BusinessException;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * PostService
 *
 * @author Sainjargal Ishdorj
 **/

public interface PostService {

    Post find(String id, HttpServletRequest req) throws BusinessException;

    Page<Post> findAll(String searchPattern, int page, int size, long minPrice, long maxPrice, float minArea, float maxArea, HttpServletRequest req);

    void add(PostAddRequest addRequest, HttpServletRequest req);

    Post update(String id, PostUpdateRequest updateRequest, HttpServletRequest req) throws BusinessException;

    void delete(String id, HttpServletRequest req) throws BusinessException;

}
