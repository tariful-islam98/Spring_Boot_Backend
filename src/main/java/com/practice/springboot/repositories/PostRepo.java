package com.practice.springboot.repositories;

import com.practice.springboot.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepo extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p JOIN p.categories c WHERE c.categoryTitle = :categoryTitle")
    Page<Post> findPostsByCategoryTitle(String categoryTitle, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.categories c WHERE c.categoryId = :categoryId")
    Page<Post> findPostsByCategoryId(@Param("categoryId") int categoryId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.user.userId = :userId")
    Page<Post> findPostsByUserId(int userId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.categories c WHERE c.categoryId = :categoryId AND p.user.userId = :userId")
    Page<Post> findPostsByCategoryAndUser(@Param("categoryId") int categoryId, @Param("userId") int userId, Pageable pageable);

    boolean existsByPostTitle(String postTitle);
}
