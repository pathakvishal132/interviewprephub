package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findByCodingQuestion_IdOrderByOrderIndexAsc(Long codingQuestionId);

    List<TestCase> findByCodingQuestion_IdAndIsHiddenOrderByOrderIndexAsc(Long codingQuestionId, Boolean isHidden);

    void deleteByCodingQuestion_Id(Long codingQuestionId);
}
