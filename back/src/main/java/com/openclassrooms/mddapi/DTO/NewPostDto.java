package com.openclassrooms.mddapi.DTO;

import lombok.Data;

@Data
public class NewPostDto {
    private String title;
    private String content;
    private Long subjectId;
}
