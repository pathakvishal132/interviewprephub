package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.CodingQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    @EntityGraph(attributePaths = "companies")
    Page<CodingQuestion> findByCompanies_Id(Long companyId, Pageable pageable);
}
