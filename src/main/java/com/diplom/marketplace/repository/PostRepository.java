package com.diplom.marketplace.repository;

import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * PostRepository
 *
 * @author Sainjargal Ishdorj
 **/

public interface PostRepository extends JpaRepository<Post, String> {

    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

//    @Query(nativeQuery = true, value = "SELECT * FROM posts WHERE user_id = :userId")
//    Page<Post> findAll(Specification<Post> specification, Pageable pageable);
//    Page<Post> findAllByUser(Specification<Post> specification, Pageable pageable, @Param(value = "userId") String userId);

}
