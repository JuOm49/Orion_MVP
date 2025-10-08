package com.openclassrooms.mddapi.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto userDto;
    private PostDto postDto;
}
