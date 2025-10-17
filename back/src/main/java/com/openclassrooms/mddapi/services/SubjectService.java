package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubjectDto;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.repositories.SubjectRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<SubjectDto> getAll() {
        Iterable<Subject> subjects = subjectRepository.findAll();

        List<SubjectDto> subjectsDto = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectsDto.add(subjectToSubjectDto(subject));
        }
        return subjectsDto;
    }

    private SubjectDto subjectToSubjectDto(Subject subject) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(subject.getId());
        subjectDto.setTitle(subject.getTitle());
        subjectDto.setDescription(subject.getDescription());
        subjectDto.setDescription(subject.getDescription());
        return subjectDto;
    }
}
