package com.interviewprephub.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_submission")
public class CodeSubmission {

    public enum Status {
        PENDING, RUNNING, PASS, FAIL, ERROR, TIME_LIMIT_EXCEEDED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(name = "coding_question_id", nullable = false)
    private Long codingQuestionId;

    @Column(nullable = false, length = 50)
    private String language;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private Status status = Status.PENDING;

    @Column(name = "test_cases_passed")
    private Integer testCasesPassed = 0;

    @Column(name = "total_test_cases")
    private Integer totalTestCases = 0;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getCodingQuestionId() { return codingQuestionId; }
    public void setCodingQuestionId(Long codingQuestionId) { this.codingQuestionId = codingQuestionId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Integer getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(Integer testCasesPassed) { this.testCasesPassed = testCasesPassed; }

    public Integer getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(Integer totalTestCases) { this.totalTestCases = totalTestCases; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
