package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object used by controllers to include a user
 * reference within post list responses sent to the front-end.
 *
 * <p>Contains only basic identification fields (id and name). Intended for
 * efficient serialization/deserialization in REST endpoints. Validation and
 * persistence are handled by other layers.</p>
 */
@Data
public class UserForPostListDto {
    private Long id;
    private String name;
}
