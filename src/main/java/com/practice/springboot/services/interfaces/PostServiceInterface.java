package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.PostDto;
import com.practice.springboot.payloads.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceInterface {

    PostResponseDto createPost(PostDto postDto);

    PostResponseDto updatePost(PostDto postDto, Integer postId);

    void deletePost(Integer postId);

    Page<PostResponseDto> getAllPost(Pageable pageable);

    PostResponseDto getPostById(Integer postId);

    Page<PostResponseDto> getFilteredPosts(Integer categoryId, Integer userId, Pageable pageable);

    Page<PostResponseDto> getPostsByCategory(Integer categoryId, Pageable pageable);

    Page<PostResponseDto> getPostsByUser(Integer userId, Pageable pageable);

    Page<PostResponseDto> getPostsByCategoryAndUser(Integer categoryId, Integer userId, Pageable pageable);
}
