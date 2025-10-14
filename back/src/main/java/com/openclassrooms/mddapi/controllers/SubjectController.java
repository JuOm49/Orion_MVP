package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubjectDto;
import com.openclassrooms.mddapi.services.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<Iterable<SubjectDto>> getSubjects() {
        if(this.subjectService.getAll().isEmpty()) {;
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(this.subjectService.getAll());
    }
}
