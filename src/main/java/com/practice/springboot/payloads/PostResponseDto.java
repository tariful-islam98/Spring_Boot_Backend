package com.practice.springboot.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private int postId;
    private String postTitle;
    private String content;
    private String imageName;
    private Date timestamp;
    private List<CategoryDto> categories;
    private int userId;
    private List<CommentDto> comments;
}
