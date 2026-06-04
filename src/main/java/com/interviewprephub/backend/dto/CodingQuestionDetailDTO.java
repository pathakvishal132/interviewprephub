package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class CodingQuestionDetailDTO {

    private Long id;
    private String title;
    private String description;
    private String difficulty;

    @JsonProperty("starter_code_java")
    private String starterCodeJava;

    @JsonProperty("starter_code_python")
    private String starterCodePython;

    @JsonProperty("starter_code_cpp")
    private String starterCodeCpp;

    @JsonProperty("starter_code_javascript")
    private String starterCodeJavascript;

    @JsonProperty("test_cases")
    private List<TestCaseDTO> testCases;

    @JsonProperty("date_of_creation")
    private LocalDateTime dateOfCreation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getStarterCodeJava() { return starterCodeJava; }
    public void setStarterCodeJava(String starterCodeJava) { this.starterCodeJava = starterCodeJava; }

    public String getStarterCodePython() { return starterCodePython; }
    public void setStarterCodePython(String starterCodePython) { this.starterCodePython = starterCodePython; }

    public String getStarterCodeCpp() { return starterCodeCpp; }
    public void setStarterCodeCpp(String starterCodeCpp) { this.starterCodeCpp = starterCodeCpp; }

    public String getStarterCodeJavascript() { return starterCodeJavascript; }
    public void setStarterCodeJavascript(String starterCodeJavascript) { this.starterCodeJavascript = starterCodeJavascript; }

    public List<TestCaseDTO> getTestCases() { return testCases; }
    public void setTestCases(List<TestCaseDTO> testCases) { this.testCases = testCases; }

    public LocalDateTime getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(LocalDateTime dateOfCreation) { this.dateOfCreation = dateOfCreation; }
}
