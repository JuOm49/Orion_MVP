package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object used by controllers to receive a new post payload
 * from the front-end.
 *
 * <p>Contains the post title, content and the identifier of the related
 * subject. Intended for serialization/deserialization in REST endpoints.
 * Input validation and persistence logic are handled by other layers.</p>
 */
@Data
public class NewPostDto {
    private String title;
    private String content;
    private Long subjectId;
}
