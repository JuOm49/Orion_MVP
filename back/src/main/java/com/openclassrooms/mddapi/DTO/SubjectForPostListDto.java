package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object used by controllers to include a subject
 * reference within post list responses sent to the front-end.
 *
 * <p>Contains only the subject identifier. Intended for efficient
 * serialization/deserialization in REST endpoints. Validation and persistence
 * are handled by other layers.</p>
 */
@Data
public class SubjectForPostListDto {
    private Long id;
}
