package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubjectDto;
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

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public ResponseEntity<Iterable<SubjectDto>> getSubjects(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<SubjectDto> subjectDto = this.subjectService.findAll();
        if(subjectDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(subjectDto);
    }
}
