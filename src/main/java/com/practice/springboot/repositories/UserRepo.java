package com.practice.springboot.repositories;

import com.practice.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts WHERE u.userId = :userId")
    User findByIdWithPosts(Integer userId);

    Optional<User> findByEmail(String email);
}
