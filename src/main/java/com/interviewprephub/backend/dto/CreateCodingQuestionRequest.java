package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CreateCodingQuestionRequest {

    private String title;

    private String description;

    private String difficulty;

    @JsonProperty("company_ids")
    private List<Long> companyIds;

    @JsonProperty("starter_code_java")
    private String starterCodeJava;

    @JsonProperty("starter_code_python")
    private String starterCodePython;

    @JsonProperty("starter_code_cpp")
    private String starterCodeCpp;

    @JsonProperty("starter_code_javascript")
    private String starterCodeJavascript;

    @JsonProperty("test_cases")
    private List<CreateTestCase> testCases;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<Long> getCompanyIds() { return companyIds; }
    public void setCompanyIds(List<Long> companyIds) { this.companyIds = companyIds; }

    public String getStarterCodeJava() { return starterCodeJava; }
    public void setStarterCodeJava(String starterCodeJava) { this.starterCodeJava = starterCodeJava; }

    public String getStarterCodePython() { return starterCodePython; }
    public void setStarterCodePython(String starterCodePython) { this.starterCodePython = starterCodePython; }

    public String getStarterCodeCpp() { return starterCodeCpp; }
    public void setStarterCodeCpp(String starterCodeCpp) { this.starterCodeCpp = starterCodeCpp; }

    public String getStarterCodeJavascript() { return starterCodeJavascript; }
    public void setStarterCodeJavascript(String starterCodeJavascript) { this.starterCodeJavascript = starterCodeJavascript; }

    public List<CreateTestCase> getTestCases() { return testCases; }
    public void setTestCases(List<CreateTestCase> testCases) { this.testCases = testCases; }

    public static class CreateTestCase {
        @JsonProperty("input_data")
        private String inputData;

        @JsonProperty("expected_output")
        private String expectedOutput;

        @JsonProperty("is_hidden")
        private Boolean isHidden = false;

        @JsonProperty("order_index")
        private Integer orderIndex = 0;

        private String explanation;

        public String getInputData() { return inputData; }
        public void setInputData(String inputData) { this.inputData = inputData; }

        public String getExpectedOutput() { return expectedOutput; }
        public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

        public Boolean getIsHidden() { return isHidden; }
        public void setIsHidden(Boolean isHidden) { this.isHidden = isHidden; }

        public Integer getOrderIndex() { return orderIndex; }
        public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }

        public String getExplanation() { return explanation; }
        public void setExplanation(String explanation) { this.explanation = explanation; }
    }
}
