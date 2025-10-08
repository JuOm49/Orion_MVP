package com.openclassrooms.mddapi.DTO;

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
    private UserDto userDto;
    private SubjectDto subjectDto;
    private List<CommentDto> commentsDto = new ArrayList<>();;
}
