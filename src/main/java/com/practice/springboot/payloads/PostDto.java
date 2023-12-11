package com.practice.springboot.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {
    private int postId;
    @NotEmpty(message = "Post Title cannot be null or empty!")
    private String postTitle;
    private String content;
    private String imageName;
    private Date timestamp;

    @NotEmpty(message = "Must provide at least one category ID!")
    private List<Integer> categoryIds;

    @Positive(message = "userId must be a valid positive integer")
    private int userId;
}
