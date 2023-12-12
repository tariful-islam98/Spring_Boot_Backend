package com.practice.springboot.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private int commentId;
    @NotEmpty(message = "Content cannot be null or empty!")
    private String content;
    private Date timestamp;
    @Positive(message = "userId must be a valid positive integer")
    private int userId;
    @Positive(message = "postId must be a valid positive integer")
    private int postId;
}
