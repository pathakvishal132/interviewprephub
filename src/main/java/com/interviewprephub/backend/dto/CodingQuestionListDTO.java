package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class CodingQuestionListDTO {

    private Long id;
    private String title;
    private String difficulty;
    private List<String> companies;

    @JsonProperty("date_of_creation")
    private LocalDateTime dateOfCreation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<String> getCompanies() { return companies; }
    public void setCompanies(List<String> companies) { this.companies = companies; }

    public LocalDateTime getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(LocalDateTime dateOfCreation) { this.dateOfCreation = dateOfCreation; }
}
