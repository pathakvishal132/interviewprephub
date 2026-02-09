package com.interviewprephub.backend.mapper;

import com.interviewprephub.backend.dto.CompanyDTO;
import com.interviewprephub.backend.dto.CompanyQuestionDTO;
import com.interviewprephub.backend.entity.CompanyQuestion;

import java.util.Set;
import java.util.stream.Collectors;

public class CompanyQuestionMapper {

    private CompanyQuestionMapper() {}

    public static CompanyQuestionDTO toDTO(CompanyQuestion entity) {
        if (entity == null) return null;

        CompanyQuestionDTO dto = new CompanyQuestionDTO();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());
        dto.setLevel(entity.getLevel().name());
        dto.setDescription(entity.getDescription());
        dto.setRole(entity.getRole());
        dto.setMinExperience(entity.getMinExperience());
        dto.setMaxExperience(entity.getMaxExperience());
        dto.setDateOfCreation(entity.getDateOfCreation());

        return dto;
    }
}
