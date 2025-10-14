package com.openclassrooms.mddapi.DTO;

import lombok.Data;

@Data
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private Long subjectId;
//    private UserDto userDto;
//    private SubjectDto subjectDto;
}
