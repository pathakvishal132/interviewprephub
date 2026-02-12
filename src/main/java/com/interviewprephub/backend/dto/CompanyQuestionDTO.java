package com.interviewprephub.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompanyQuestionDTO {


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CompanyMiniDTO> getCompanies() {
		return companies;
	}

	public void setCompanies(List<CompanyMiniDTO> companies) {
		this.companies = companies;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public LocalDateTime getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(LocalDateTime dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getMinExperience() {
		return minExperience;
	}

	public void setMinExperience(int minExperience) {
		this.minExperience = minExperience;
	}

	public int getMaxExperience() {
		return maxExperience;
	}

	public void setMaxExperience(int maxExperience) {
		this.maxExperience = maxExperience;
	}

	private Long id;
    private List<CompanyMiniDTO> companies;
    private String level;
    private String question;
    private String answer;

    @JsonProperty("date_of_creation")
    private LocalDateTime dateOfCreation;

    private String description;
    private String role;

    @JsonProperty("min_experience")
    private int minExperience;

    @JsonProperty("max_experience")
    private int maxExperience;

    
}
