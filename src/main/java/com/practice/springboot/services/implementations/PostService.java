package com.practice.springboot.services.implementations;

import com.practice.springboot.entities.Category;
import com.practice.springboot.entities.Post;
import com.practice.springboot.entities.User;
import com.practice.springboot.exceptions.NotNullViolationException;
import com.practice.springboot.payloads.CategoryDto;
import com.practice.springboot.payloads.PostDto;
import com.practice.springboot.payloads.PostResponseDto;
import com.practice.springboot.payloads.UserDto;
import com.practice.springboot.repositories.CategoryRepo;
import com.practice.springboot.repositories.PostRepo;
import com.practice.springboot.services.interfaces.PostServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PostService implements PostServiceInterface {

    private PostRepo postRepo;
    private ModelMapper modelMapper;
    private UserService userService;
    private CategoryRepo categoryRepo;

    @Autowired
    public PostService(PostRepo postRepo, ModelMapper modelMapper, UserService userService, CategoryRepo categoryRepo) {
        this.postRepo = postRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public PostResponseDto createPost(PostDto postDto) {
        Post postEntity = modelMapper.map(postDto, Post.class);

        try {
            UserDto userDto = userService.getUserById(postDto.getUserId());
            User user = modelMapper.map(userDto, User.class);

            List<Category> categories = categoryRepo.findAllById(postDto.getCategoryIds());

            postEntity.setUser(user);
            postEntity.setCategories(categories);
            postEntity.setTimestamp(new Date());

            try {
                Post savedPost = postRepo.save(postEntity);

                return convertToPostResponseDto(savedPost);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post Title already exists", e);
            } catch (NotNullViolationException e) {
                throw new NotNullViolationException("Error creating post: postTitle field is required and cannot be blank", HttpStatus.BAD_REQUEST);
            }

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more categories not found", e);
        }
    }

    @Override
    public PostResponseDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with id: " + postId));

        postDto.setUserId(post.getUser().getUserId());
        postDto.setTimestamp(post.getTimestamp());

        if (postDto.getPostTitle() != null && !postDto.getPostTitle().equals(post.getPostTitle())) {

            if (postRepo.existsByPostTitle(postDto.getPostTitle())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post Title already exists");
            }

            post.setPostTitle(postDto.getPostTitle());
        }

        if (postDto.getCategoryIds() != null) {
            try {
                List<Category> updatedCategories = categoryRepo.findAllById(postDto.getCategoryIds());
                post.setCategories(updatedCategories);
            } catch (NoSuchElementException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more categories not found", e);
            }
        }

        if (postDto.getContent() != null) post.setContent(postDto.getContent());
        if (postDto.getImageName() != null) post.setImageName(postDto.getImageName());

        try {
            Post updatedPost = postRepo.save(post);

            return convertToPostResponseDto(updatedPost);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Post Title already exists", e);
        } catch (NotNullViolationException e) {
            throw new NotNullViolationException("Error updating post: postTitle field is required and cannot be blank", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with id: " + postId));

        postRepo.delete(post);
    }

    @Override
    public Page<PostResponseDto> getAllPost(Pageable pageable) {
        Page<Post> postList = postRepo.findAll(pageable);

        return postList.map(this::convertToPostResponseDto);
    }

    @Override
    public PostResponseDto getPostById(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with id: " + postId));

        return convertToPostResponseDto(post);
    }

    @Override
    public Page<PostResponseDto> getFilteredPosts(Integer categoryId, Integer userId, Pageable pageable) {
        try {
            if (categoryId != null && userId != null) {
                return getPostsByCategoryAndUser(categoryId, userId, pageable);
            } else if (categoryId != null) {
                return getPostsByCategory(categoryId, pageable);
            } else if (userId != null) {
                return getPostsByUser(userId, pageable);
            } else {
                return getAllPost(pageable);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more parameter is invalid!");
        }
    }

    @Override
    public Page<PostResponseDto> getPostsByCategory(Integer categoryId, Pageable pageable) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        Page<Post> posts = postRepo.findPostsByCategoryId(categoryId, pageable);

        return posts.map(this::convertToPostResponseDto);
    }

    @Override
    public Page<PostResponseDto> getPostsByUser(Integer userId, Pageable pageable) {
        UserDto userDto = userService.getUserById(userId);

        Page<Post> posts = postRepo.findPostsByUserId(userId, pageable);

        return posts.map(this::convertToPostResponseDto);
    }

    @Override
    public Page<PostResponseDto> getPostsByCategoryAndUser(Integer categoryId, Integer userId, Pageable pageable) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId));

        UserDto userDto = userService.getUserById(userId);

        Page<Post> posts = postRepo.findPostsByCategoryAndUser(categoryId, userId, pageable);

        return posts.map(this::convertToPostResponseDto);
    }

    private PostResponseDto convertToPostResponseDto(Post post) {
        PostResponseDto postResponseDto = modelMapper.map(post, PostResponseDto.class);
        postResponseDto.setCategories(modelMapper.map(post.getCategories(), new TypeToken<List<CategoryDto>>() {}.getType()));
        return postResponseDto;
    }
}
