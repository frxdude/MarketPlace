package com.diplom.marketplace.repository;

import com.diplom.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(nativeQuery = true, value = "SELECT * FROM user u WHERE u.first_name LIKE :param% OR u.last_name LIKE :param% OR u.email LIKE :param% OR u.phone LIKE :param%")
    @Query(nativeQuery = true, value = "SELECT * FROM users u INNER JOIN user_roles ur on u.id = ur.user_id WHERE ur.roles = ?1")
    Page<User> findAll(@Param("role") Long role, Specification<User> specification, Pageable pageable);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

}