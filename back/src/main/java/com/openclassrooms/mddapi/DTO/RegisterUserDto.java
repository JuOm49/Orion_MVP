package com.openclassrooms.mddapi.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegisterUserDto {
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> commentsDto = new ArrayList<>();
    private List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
    private List<PostDto> postsDto = new ArrayList<>();
}
