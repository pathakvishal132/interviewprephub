package com.interviewprephub.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewprephub.backend.entity.SubmissionTracker;

public interface QuestionRepository extends JpaRepository<SubmissionTracker, Long>{
	
	Optional<SubmissionTracker> findByUserIdAndDate(String userId, LocalDate date);

    List<SubmissionTracker> findByUserIdOrderByDate(String userId);

}
