package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubjectDto;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service responsible for retrieving and converting Subject entities.
 *
 * <p>This service delegates persistence operations to {@code SubjectRepository}
 * and can provide JPA references to {@code Subject} entities via {@code EntityManager}.</p>
 */
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Construct a new {@code SubjectService}.
     *
     * @param subjectRepository repository used to persist and query {@code Subject} entities
     */
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    /**
     * Find a subject by its identifier.
     *
     * @param id identifier of the subject to retrieve
     * @return an {@code Optional} containing the {@code Subject} if found, otherwise {@code Optional.empty()}
     */
    public Optional<Subject> findById(Long id) {
        return subjectRepository.findById(id);
    }

    /**
     * Return all subjects and convert them to DTOs.
     *
     * <p>The method iterates over all {@code Subject} entities returned by the repository
     * and maps each to a {@code SubjectDto}.</p>
     *
     * @return a {@code List} of {@code SubjectDto} representing all subjects
     */
    public List<SubjectDto> findAll() {
        Iterable<Subject> subjects = subjectRepository.findAll();

        List<SubjectDto> subjectsDto = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDto.add(subjectToSubjectDto(subject));
        }
        return subjectsDto;
    }

    /**
     * Obtain a JPA reference (proxy) for a {@code Subject} with the given identifier.
     *
     * <p>This uses {@code EntityManager#getReference} to return a lazily-initialized proxy.
     * Accessing properties of the returned proxy will trigger a database load and may throw
     * {@code jakarta.persistence.EntityNotFoundException} if the entity does not exist.</p>
     *
     * @param id identifier of the subject
     * @return a {@code Subject} instance which may be a proxy; accessing it may load the entity
     * @throws jakarta.persistence.EntityNotFoundException if the proxy is accessed and the entity is absent
     */
    public Subject getReferenceById(Long id) {
        return entityManager.getReference(Subject.class, id);
    }

    /**
     * Convert a {@code Subject} entity to a {@code SubjectDto}.
     *
     * @param subject the {@code Subject} entity to convert
     * @return a {@code SubjectDto} containing the subject's public fields
     */
    private SubjectDto subjectToSubjectDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getId());
        subjectDto.setTitle(subject.getTitle());
        subjectDto.setDescription(subject.getDescription());
        subjectDto.setDescription(subject.getDescription());
        return subjectDto;
    }
}
