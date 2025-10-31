package com.openclassrooms.mddapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object used by controllers to send and receive subject payloads
 * between the API and the front-end.
 *
 * <p>Contains subject data and collections of related posts and
 * subscriptions for efficient serialization in REST endpoints. Input validation
 * and persistence are handled by other layers.</p>
 */
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
