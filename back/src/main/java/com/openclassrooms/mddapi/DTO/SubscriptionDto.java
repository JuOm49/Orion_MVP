package com.openclassrooms.mddapi.DTO;

import lombok.Data;

@Data
public class SubscriptionDto {
    private Long id;
    private UserDto userDto;
    private SubjectDto subjectDto;
}
