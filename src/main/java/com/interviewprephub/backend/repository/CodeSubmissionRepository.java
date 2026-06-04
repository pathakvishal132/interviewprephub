package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.CodeSubmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {

    Page<CodeSubmission> findByUserIdOrderBySubmittedAtDesc(String userId, Pageable pageable);

    Page<CodeSubmission> findByCodingQuestionIdOrderBySubmittedAtDesc(Long codingQuestionId, Pageable pageable);

    List<CodeSubmission> findByUserIdAndSubmittedAtAfterOrderBySubmittedAtAsc(String userId, LocalDateTime since);
}
