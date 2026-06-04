package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestCaseDTO {

    private Long id;

    @JsonProperty("input_data")
    private String inputData;

    @JsonProperty("expected_output")
    private String expectedOutput;

    @JsonProperty("is_hidden")
    private Boolean isHidden;

    @JsonProperty("order_index")
    private Integer orderIndex;

    private String explanation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
