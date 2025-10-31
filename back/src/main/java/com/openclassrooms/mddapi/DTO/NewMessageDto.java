package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object used by controllers to receive a new message payload
 * from the front-end.
 *
 * <p>Contains the message text submitted by a client when creating or updating
 * a message. Intended for simple serialization/deserialization in REST endpoints.
 * Input validation and processing are handled by other layers.</p>
 */
@Data
public class NewMessageDto {
    private String message;
}
