package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object used by controllers to send and receive comment payloads
 * between the API and the front-end.
 *
 * <p>Contains basic comment data and lightweight references to the author and
 * the related post. Designed for serialization/deserialization in REST endpoints.</p>
 */
@Data
public class CommentDto {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("user")
    private UserDto userDto;
    @JsonProperty("post")
    private PostDto postDto;
}
