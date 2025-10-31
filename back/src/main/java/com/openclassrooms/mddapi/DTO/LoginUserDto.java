package com.openclassrooms.mddapi.DTO;

import lombok.Data;

/**
 * Data Transfer Object representing login credentials submitted by a client front-end.
 *
 * <p>Contains an identifier (username or email) and a password. Intended for use
 * by authentication endpoints to receive login payloads from the front-end.
 * Input validation and authentication logic are handled elsewhere.</p>
 */
@Data
public class LoginUserDto {
    private String identifier;
    private String password;
}
