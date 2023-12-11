package com.practice.springboot.services.interfaces;

import com.practice.springboot.payloads.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceInterface {

    PostDto createPost(PostDto postDto);

    PostDto updatePost(PostDto postDto, Integer postId);

    void deletePost(Integer postId);

    Page<PostDto> getAllPost(Pageable pageable);

    PostDto getPostById(Integer postId);

    Page<PostDto> getFilteredPosts(Integer categoryId, Integer userId, Pageable pageable);

    Page<PostDto> getPostsByCategory(Integer categoryId, Pageable pageable);

    Page<PostDto> getPostsByUser(Integer userId, Pageable pageable);

    Page<PostDto> getPostsByCategoryAndUser(Integer categoryId, Integer userId, Pageable pageable);
}
