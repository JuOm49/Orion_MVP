package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
