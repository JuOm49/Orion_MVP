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

/**
 * REST controller for managing subjects.
 * Provides endpoints for retrieving available subjects.
 * All endpoints require authentication handled by AuthByIdInterceptor.
 */
@RestController
@RequestMapping("/api")
public class SubjectController {

    private final SubjectService subjectService;
    private final AuthenticationService authenticationService;

    /**
     * Constructs a new SubjectController with the specified services.
     *
     * @param subjectService the service for subject operations
     * @param authenticationService the service responsible for authentication management,
     *                              used to extract the user ID from the HTTP request with
     *                              {@code getUserIdFromHttpServletRequest()}
     */
    public SubjectController(SubjectService subjectService, AuthenticationService authenticationService) {
        this.subjectService = subjectService;
        this.authenticationService = authenticationService;
    }

    /**
     * Return all available subjects.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @return ResponseEntity containing a list of subjects or 204 No Content if no subjects found
     */
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
