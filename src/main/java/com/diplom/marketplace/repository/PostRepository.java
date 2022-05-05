package com.diplom.marketplace.repository;

import com.diplom.marketplace.entity.Post;
import com.diplom.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostRepository
 *
 * @author Sainjargal Ishdorj
 **/

public interface PostRepository extends JpaRepository<Post, String> {

    Page<Post> findAll(Specification<Post> specification, Pageable pageable);

}
