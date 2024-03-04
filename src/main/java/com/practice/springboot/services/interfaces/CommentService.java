package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);

    CommentDto updateComment(CommentDto commentDto, Integer commentId);

    void deleteComment(Integer commentId);

    Page<CommentDto> getAllComments(Pageable pageable);

    CommentDto getCommentById(Integer commentId);

    Page<CommentDto> getFilteredComments(Integer postId, Integer userId, Pageable pageable);

    Page<CommentDto> getCommentsForPost(Integer postId, Pageable pageable);

    Page<CommentDto> getCommentsByUser(Integer userId, Pageable pageable);

    Page<CommentDto> getCommentsByPostAndUser(Integer postId, Integer userId, Pageable pageable);
}
