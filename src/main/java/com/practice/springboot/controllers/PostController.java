package com.practice.springboot.controllers;

import com.practice.springboot.config.Constants;
import com.practice.springboot.payloads.PostDto;
import com.practice.springboot.payloads.PostResponseDto;
import com.practice.springboot.services.interfaces.PostServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostServiceInterface postService;

    @Autowired
    public PostController(PostServiceInterface postService) {
        this.postService = postService;
    }

    @PostMapping("/")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostDto postDto) {
        PostResponseDto createPost = postService.createPost(postDto);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        PostResponseDto updatePost = postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = Constants.DEFAULT_OFFSET, required = false) Integer offset,
            @RequestParam(defaultValue = Constants.DEFAULT_LIMIT, required = false) Integer limit,
            @RequestParam(defaultValue = Constants.DEFAULT_POST_SORT_BY, required = false) String sortBy) {

        try {
            Page<PostResponseDto> posts = postService.getFilteredPosts(categoryId, userId, PageRequest.of(offset, limit, Sort.by(sortBy)));
            return new ResponseEntity<>(posts.getContent(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more parameter is invalid!");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Integer postId) {
        PostResponseDto post = postService.getPostById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
