package com.practice.springboot.repositories;

import com.practice.springboot.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    boolean existsByCategoryTitleIgnoreCase(String categoryTitle);

    boolean existsByCategoryTitleIgnoreCaseAndCategoryIdNot(String categoryTitle, int categoryId);

    @Query("SELECT c FROM Category c JOIN c.posts p WHERE p.postTitle = :postTitle")
    List<Category> findCategoriesByPostTitle(String postTitle);

    @Query("SELECT c FROM Category c JOIN c.posts p WHERE p.postId = :postId")
    List<Category> findCategoriesByPostId(@Param("postId") int postId);
}
