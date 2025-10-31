package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object used by controllers to send a concise post representation
 * to the front-end (e.g. for lists). Includes core post fields and
 * references to the author and subject for efficient serialization in REST
 * endpoints. Timestamps are set by the server; additional validation is handled
 * by other layers.
 */
@Data
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    @JsonProperty("user")
    private UserForPostListDto userForPostListDto;
    @JsonProperty("subject")
    private SubjectForPostListDto subjectForPostListDtoList;
}
