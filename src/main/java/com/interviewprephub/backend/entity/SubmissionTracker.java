package com.interviewprephub.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "submission_tracker",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "date"})
    }
)
public class SubmissionTracker {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @Column(name = "submission_count", nullable = false)
    private Integer submissionCount = 0;

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getSubmissionCount() {
		return submissionCount;
	}

	public void setSubmissionCount(Integer submissionCount) {
		this.submissionCount = submissionCount;
	}
}
