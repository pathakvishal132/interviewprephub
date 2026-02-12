package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.CompanyQuestion;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyQuestionRepository
extends JpaRepository<CompanyQuestion, Long>,
        JpaSpecificationExecutor<CompanyQuestion>{

	@EntityGraph(attributePaths = "companies")
	Page<CompanyQuestion> findByCompanies_Id(Long companyId, Pageable pageable);
	
    Page<CompanyQuestion>
    findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String question,
            String answer,
            String description,
            Pageable pageable
    );
    List<CompanyQuestion> findAllByCompanies_Id(Long companyId);
    
    @EntityGraph(attributePaths = "companies")
    Page<CompanyQuestion> findAll(Specification<CompanyQuestion> spec, Pageable pageable);


    
}
