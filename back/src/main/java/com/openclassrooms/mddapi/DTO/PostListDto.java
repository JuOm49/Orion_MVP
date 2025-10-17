package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    @JsonProperty("user")
    private UserForPostListDto userForPostListDto;
}
