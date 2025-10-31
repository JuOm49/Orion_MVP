package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object used by controllers to send and receive post payloads
 * between the API and the front-end.
 *
 * <p>Contains post metadata, references to the author and subject,
 * and a list of comments. Designed for serialization/deserialization in REST
 * endpoints. Timestamps are set by the server; input validation and persistence
 * are handled by other layers.</p>
 */
@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonProperty("user")
    private UserDto userDto;
    @JsonProperty("subject")
    private SubjectDto subjectDto;
    @JsonProperty("comments")
    private List<CommentDto> commentsDto = new ArrayList<>();;
}
