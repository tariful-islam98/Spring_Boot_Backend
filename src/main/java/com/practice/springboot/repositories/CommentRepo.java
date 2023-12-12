package com.practice.springboot.repositories;

import com.practice.springboot.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    Page<Comment> findByPostId(@Param("postId") int postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId")
    Page<Comment> findByUserId(@Param("userId") int userId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId AND c.user.userId = :userId")
    Page<Comment> findByPostAndUser(@Param("postId") int postId, @Param("userId") int userId, Pageable pageable);
}
