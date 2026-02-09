package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.CompanyQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyQuestionRepository extends JpaRepository<CompanyQuestion, Long> {

	@EntityGraph(attributePaths = "companies")
	Page<CompanyQuestion> findByCompanies_Id(Long companyId, Pageable pageable);

    // ✅ Search questions with pagination
    @Query("""
        SELECT q FROM CompanyQuestion q
        WHERE LOWER(q.question) LIKE LOWER(CONCAT('%', :word, '%'))
           OR LOWER(q.answer) LIKE LOWER(CONCAT('%', :word, '%'))
           OR LOWER(q.description) LIKE LOWER(CONCAT('%', :word, '%'))
    """)
    Page<CompanyQuestion> searchQuestions(
            @Param("word") String word,
            Pageable pageable
    );

    // ✅ Search with pagination
    Page<CompanyQuestion>
    findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String question,
            String answer,
            String description,
            Pageable pageable
    );
    
}
