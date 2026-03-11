package com.interviewprephub.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "topic_progress",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "domain", "subdomain"})
    }
)
public class TopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @Column(nullable = false, length = 100)
    private String domain;

    @Column(nullable = false, length = 100)
    private String subdomain;

    @Column(name = "questions_attempted", nullable = false)
    private Integer questionsAttempted = 0;

    @Column(name = "questions_solved", nullable = false)
    private Integer questionsSolved = 0;

    @Column(name = "last_attempt_date")
    private LocalDate lastAttemptDate;

    @Column(name = "total_submissions", nullable = false)
    private Integer totalSubmissions = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public Integer getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(Integer questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public Integer getQuestionsSolved() {
        return questionsSolved;
    }

    public void setQuestionsSolved(Integer questionsSolved) {
        this.questionsSolved = questionsSolved;
    }

    public LocalDate getLastAttemptDate() {
        return lastAttemptDate;
    }

    public void setLastAttemptDate(LocalDate lastAttemptDate) {
        this.lastAttemptDate = lastAttemptDate;
    }

    public Integer getTotalSubmissions() {
        return totalSubmissions;
    }

    public void setTotalSubmissions(Integer totalSubmissions) {
        this.totalSubmissions = totalSubmissions;
    }
}
