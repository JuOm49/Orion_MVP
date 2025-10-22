package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectDto {
    private Long id;
    private String title;
    private String description;
    @JsonProperty("posts")
    private List<PostDto> postsDto = new ArrayList<>();
    @JsonProperty("subscriptions")
    private List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
}
