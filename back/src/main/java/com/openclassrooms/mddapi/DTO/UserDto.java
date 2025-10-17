package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("comments")
    private List<CommentDto> commentsDto = new ArrayList<>();
    @JsonProperty("subscriptions")
    private List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
    @JsonProperty("posts")
    private List<PostDto> postsDto = new ArrayList<>();
}
