package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.SubmissionTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionTrackerRepository
        extends JpaRepository<SubmissionTracker, Long> {

    Optional<SubmissionTracker> findByUserIdAndDate(
            String userId,
            LocalDate date
    );

    List<SubmissionTracker> findByUserIdOrderByDate(String userId);
}
