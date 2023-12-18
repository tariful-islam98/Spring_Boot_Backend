package com.practice.springboot.controllers;

import com.practice.springboot.utils.Constants;
import com.practice.springboot.payloads.CommentDto;
import com.practice.springboot.services.implementations.CommentService;
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
@RequestMapping("/api/comments")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/")
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updatedComment(@RequestBody CommentDto commentDto, @PathVariable Integer commentId) {
        CommentDto updatedComment = commentService.updateComment(commentDto, commentId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<CommentDto>> getAllComments(
            @RequestParam(required = false) Integer postId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_OFFSET) Integer offset,
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_LIMIT) Integer limit,
            @RequestParam(required = false, defaultValue = Constants.DEFAULT_COMMENT_SORT_BY) String sortBy) {
        try {
            Page<CommentDto> comments = commentService.getFilteredComments(postId, userId, PageRequest.of(offset, limit, Sort.by(sortBy)));
            return new ResponseEntity<>(comments.getContent(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more parameter is invalid!");
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Integer commentId) {
        CommentDto commentDto = commentService.getCommentById(commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
