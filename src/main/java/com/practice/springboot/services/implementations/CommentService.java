package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.Comment;
import com.practice.springboot.entities.Post;
import com.practice.springboot.entities.User;
import com.practice.springboot.exceptions.NotNullViolationException;
import com.practice.springboot.payloads.CommentDto;
import com.practice.springboot.payloads.PostResponseDto;
import com.practice.springboot.payloads.UserDto;
import com.practice.springboot.repositories.CommentRepo;
import com.practice.springboot.services.interfaces.CommentServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class CommentService implements CommentServiceInterface {
    private CommentRepo commentRepo;
    private ModelMapper modelMapper;
    private UserService userService;
    private PostService postService;

    @Autowired
    public CommentService(CommentRepo commentRepo, ModelMapper modelMapper, UserService userService, PostService postService) {
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.postService = postService;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) {
        Comment commentEntity = modelMapper.map(commentDto, Comment.class);

        UserDto userDto = userService.getUserById(commentDto.getUserId());
        PostResponseDto postDto = postService.getPostById(commentDto.getPostId());

        commentEntity.setUser(modelMapper.map(userDto, User.class));
        commentEntity.setPost(modelMapper.map(postDto, Post.class));
        commentEntity.setTimestamp(new Date());

        Comment savedComment = commentRepo.save(commentEntity);

        return modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + commentId));

        commentDto.setUserId(comment.getUser().getUserId());
        commentDto.setPostId(comment.getPost().getPostId());
        commentDto.setTimestamp(comment.getTimestamp());

        if (commentDto.getContent() != null && !commentDto.getContent().isBlank()){
            comment.setContent(commentDto.getContent());
        }

        try {
            Comment updatedComment = commentRepo.save(comment);

            return modelMapper.map(updatedComment, CommentDto.class);
        }catch (NotNullViolationException e){
            throw new NotNullViolationException("Error updating comment: content field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + commentId));

        commentRepo.delete(comment);
    }

    @Override
    public Page<CommentDto> getAllComments(Pageable pageable) {
        Page<Comment> comments = commentRepo.findAll(pageable);

        return comments.map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @Override
    public CommentDto getCommentById(Integer commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + commentId));
        
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public Page<CommentDto> getFilteredComments(Integer postId, Integer userId, Pageable pageable) {
        try {
            if (postId != null && userId != null) {
                return getCommentsByPostAndUser(postId, userId, pageable);
            } else if (postId != null) {
                return getCommentsForPost(postId, pageable);
            } else if (userId != null) {
                return getCommentsByUser(userId, pageable);
            } else {
                return getAllComments(pageable);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more parameter is invalid!");
        }
    }

    @Override
    public Page<CommentDto> getCommentsForPost(Integer postId, Pageable pageable) {
        PostResponseDto postResponseDto = postService.getPostById(postId);
        Page<Comment> comments = commentRepo.findByPostId(postId, pageable);

        return comments.map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @Override
    public Page<CommentDto> getCommentsByUser(Integer userId, Pageable pageable) {
        userService.getUserById(userId);
        Page<Comment> comments = commentRepo.findByUserId(userId, pageable);
        
        return comments.map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @Override
    public Page<CommentDto> getCommentsByPostAndUser(Integer postId, Integer userId, Pageable pageable) {
        postService.getPostById(postId);
        userService.getUserById(userId);
        Page<Comment> comments = commentRepo.findByPostAndUser(postId, userId, pageable);
        
        return comments.map(comment -> modelMapper.map(comment, CommentDto.class));
    }
}
