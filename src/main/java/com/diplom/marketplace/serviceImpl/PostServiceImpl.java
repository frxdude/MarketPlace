package com.diplom.marketplace.serviceImpl;

import com.diplom.marketplace.dto.request.post.PostAddRequest;
import com.diplom.marketplace.dto.request.post.PostUpdateRequest;
import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.entity.PostImage;
import com.diplom.marketplace.entity.enums.PostTypes;
import com.diplom.marketplace.exception.BusinessException;
import com.diplom.marketplace.helper.Localization;
import com.diplom.marketplace.helper.specification.PostSpecification;
import com.diplom.marketplace.helper.specification.SearchCriteria;
import com.diplom.marketplace.repository.PostImageRepository;
import com.diplom.marketplace.repository.PostRepository;
import com.diplom.marketplace.service.PostService;
import com.diplom.marketplace.util.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * PostServiceImpl
 *
 * @author Sainjargal Ishdorj
 **/

@Service
public class PostServiceImpl implements PostService {

    PostRepository repository;
    PostImageRepository postImageRepository;
    PostSpecification minPriceSpec, maxPriceSpec, minAreaSpec, maxAreaSpec, titleSpec;
    Localization localization;

    @Autowired
    public PostServiceImpl(PostRepository repository, PostImageRepository postImageRepository, PostSpecification minPriceSpec, PostSpecification maxPriceSpec, PostSpecification minAreaSpec, PostSpecification maxAreaSpec, PostSpecification titleSpec, Localization localization) {
        this.repository = repository;
        this.postImageRepository = postImageRepository;
        this.minPriceSpec = minPriceSpec;
        this.maxPriceSpec = maxPriceSpec;
        this.minAreaSpec = minAreaSpec;
        this.maxAreaSpec = maxAreaSpec;
        this.titleSpec = titleSpec;
        this.localization = localization;
    }

    public Post find(String id, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[find][input][id=" + id + "]");
            Post post = repository.findById(id)
                    .orElseThrow(() -> new BusinessException(localization.getMessage("data.not.found"), "Post data not found"));
            Logger.info(getClass().getName(), "[find][output][Post(id=" + post.getId() + "]");
            return post;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[find][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[find][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    public Page<Post> findAll(String searchPattern, int page, int size, long minPrice, long maxPrice, float minArea, float maxArea, HttpServletRequest req) {
        try {
            Logger.info(getClass().getName(), "[findAll][input][page=" + page + ", size=" + size + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + ", minArea=" + minArea + ", maxArea=" + maxArea + "]");

            titleSpec = StringUtils.isNotBlank(searchPattern)
                    ? new PostSpecification(new SearchCriteria("title", ".%", searchPattern))
                    : new PostSpecification();

            minPriceSpec = minPrice != 0
                    ? new PostSpecification(new SearchCriteria("price", "<", minPrice))
                    : new PostSpecification();

            maxPriceSpec = maxPrice != 0
                    ? new PostSpecification(new SearchCriteria("price", ">", maxPrice))
                    : new PostSpecification();

            minAreaSpec = minArea != 0
                    ? new PostSpecification(new SearchCriteria("area", ">", minArea))
                    : new PostSpecification();

            maxAreaSpec = minArea != 0
                    ? new PostSpecification(new SearchCriteria("area", "<", maxArea))
                    : new PostSpecification();

            Page<Post> postPage = repository.findAll(minPriceSpec.and(maxPriceSpec.and(maxAreaSpec.and(minAreaSpec.and(titleSpec)))), PageRequest.of(page, size, Sort.by("id").descending()));
            Logger.info(getClass().getName(), "[findAll][output][totalElements" + postPage.getTotalElements() + ", size=" + postPage.getSize() + "]");
            return postPage;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[findAll][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    public void add(PostAddRequest addRequest, HttpServletRequest req) {
        try {
            Logger.info(getClass().getName(), "[add][input][" + addRequest.toString() + "]");
            Post post = Post.builder()
                    .title(addRequest.getTitle())
                    .description(addRequest.getDescription())
                    .address(addRequest.getAddress())
                    .area(addRequest.getArea())
                    .rooms(addRequest.getRooms())
                    .postTypes(Collections.singletonList(addRequest.getPostType() == 0 ?  PostTypes.POST_PLACE : PostTypes.POST_APARTMENT))
                    .build();
            repository.save(post);

            for(int i = 0; i < addRequest.getPostImages().size() ; i++) {
                PostImage postImage = PostImage.builder()
                        .image(addRequest.getPostImages().get(i).getImage())
                        .isPoster(addRequest.getPostImages().get(i).isPoster())
                        .post(post)
                        .build();
                postImageRepository.save(postImage);
            }
            Logger.info(getClass().getName(), "[add][output][]");
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[add][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    public Post update(String id, PostUpdateRequest updateRequest, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[update][input][id=" + id + "]");
            Post post = find(id, req);
            post.setArea(updateRequest.getArea());
            post.setTitle(updateRequest.getTitle());
            post.setDescription(updateRequest.getDescription());
            post.setPrice(updateRequest.getPrice());
            post.setRooms(updateRequest.getRooms());
            post.setAddress(updateRequest.getAddress());
            repository.save(post);
            Logger.info(getClass().getName(), "[update][output][]");
            return post;
        } catch (BusinessException ex) {
            Logger.warn(getClass().getName(), "[update][" + ex.reason + "]");
            throw ex;
        } catch (Exception ex) {
            Logger.fatal(getClass().getName(), "[update][" + ex.getMessage() + "]", ex);
            throw ex;
        }
    }

    /**
     * @param id  String
     * @param req servlet request
     * @throws BusinessException when Post not found
     * @author Sainjargal Ishdorj
     **/

    public void delete(String id, HttpServletRequest req) throws BusinessException {
        try {
            Logger.info(getClass().getName(), "[delete][input][id=" + id + "]");
            Post post = find(id, req);
            repository.delete(post);
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
