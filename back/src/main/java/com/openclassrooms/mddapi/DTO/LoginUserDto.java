package com.openclassrooms.mddapi.DTO;

import lombok.Data;

@Data
public class LoginUserDto {
    private String identifier;
    private String password;
}
