package com.interviewprephub.backend.entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_reviews")
public class CompanyReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false, length = 100)
    private String companyId;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "job_role", nullable = false, length = 255)
    private String jobRole;

    private String interviewLevel;

    @Column(columnDefinition = "TEXT")
    private String questionsAsked;

    @Column(columnDefinition = "TEXT")
    private String companyCulture;

    private String companyPayroll;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // -------- getters & setters --------

    public Long getId() {
        return id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getInterviewLevel() {
        return interviewLevel;
    }

    public void setInterviewLevel(String interviewLevel) {
        this.interviewLevel = interviewLevel;
    }

    public String getQuestionsAsked() {
        return questionsAsked;
    }

    public void setQuestionsAsked(String questionsAsked) {
        this.questionsAsked = questionsAsked;
    }

    public String getCompanyCulture() {
        return companyCulture;
    }

    public void setCompanyCulture(String companyCulture) {
        this.companyCulture = companyCulture;
    }

    public String getCompanyPayroll() {
        return companyPayroll;
    }

    public void setCompanyPayroll(String companyPayroll) {
        this.companyPayroll = companyPayroll;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return companyName + " - " + jobRole;
    }
}
