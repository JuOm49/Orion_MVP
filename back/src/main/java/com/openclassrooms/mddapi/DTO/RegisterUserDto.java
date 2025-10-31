package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object used by controllers to receive a user registration
 * payload from the front-end.
 *
 * <p>Contains basic user details (name, email, password), timestamps and
 * references to related entities (comments, subscriptions, posts).
 * Intended for serialization/deserialization in REST endpoints. Input validation
 * and persistence are handled by other layers.</p>
 */
@Data
public class RegisterUserDto {
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("comments")
    private List<CommentDto> commentsDto = new ArrayList<>();
    @JsonProperty("subscriptions")
    private List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
    @JsonProperty("posts")
    private List<PostDto> postsDto = new ArrayList<>();
}
