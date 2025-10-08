package com.openclassrooms.mddapi.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectDto {
    private Long id;
    private String title;
    private String description;
    private List<PostDto> postsDto = new ArrayList<>();
    private List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
}
