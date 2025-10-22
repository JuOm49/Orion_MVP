package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubjectDto;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.services.SubjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubjectController {

    private final SubjectService subjectService;
    private final AuthenticationService authenticationService;

    public SubjectController(SubjectService subjectService, AuthenticationService authenticationService) {
        this.subjectService = subjectService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<Iterable<SubjectDto>> getSubjects(HttpServletRequest request) {
        authenticationService.getUserIdFromHttpServletRequest(request);

        List<SubjectDto> subjectDto = this.subjectService.findAll();
        if(subjectDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(subjectDto);
    }
}
