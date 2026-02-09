package com.interviewprephub.backend.mapper;

import com.interviewprephub.backend.dto.CompanyDTO;
import com.interviewprephub.backend.entity.Company;

public class CompanyMapper {

    private CompanyMapper() {}

    public static CompanyDTO toDTO(Company company) {
        if (company == null) return null;

        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        return dto;
    }
}
