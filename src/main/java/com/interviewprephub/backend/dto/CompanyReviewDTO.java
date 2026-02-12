package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CompanyReviewDTO {

    private Long id;


	@JsonProperty("job_role")
    private String jobRole;

    @JsonProperty("interview_level")
    private String interviewLevel;

    @JsonProperty("questions_asked")
    private String questionsAsked;

    @JsonProperty("company_culture")
    private String companyCulture;

    @JsonProperty("company_payroll")
    private String companyPayroll;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("company_name")
    private String companyName;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


    // getters & setters
}
