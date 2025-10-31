package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object used by controllers to send and receive subscription
 * payloads between the API and the front-end.
 *
 * <p>Contains identifiers linking a user to a subject. Intended for
 * serialization/deserialization in REST endpoints. Validation and persistence
 * are handled by other layers.</p>
 */
@Data
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private Long subjectId;
}
