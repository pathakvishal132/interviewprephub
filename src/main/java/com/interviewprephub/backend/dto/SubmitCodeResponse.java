package com.interviewprephub.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SubmitCodeResponse {

    private String status;

    @JsonProperty("test_cases_passed")
    private int testCasesPassed;

    @JsonProperty("total_test_cases")
    private int totalTestCases;

    private String output;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("execution_time_ms")
    private Long executionTimeMs;

    @JsonProperty("test_case_results")
    private List<TestCaseResult> testCaseResults;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }

    public int getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public List<TestCaseResult> getTestCaseResults() { return testCaseResults; }
    public void setTestCaseResults(List<TestCaseResult> testCaseResults) { this.testCaseResults = testCaseResults; }

    public static class TestCaseResult {
        @JsonProperty("test_case_id")
        private Long testCaseId;

        private boolean passed;

        @JsonProperty("actual_output")
        private String actualOutput;

        @JsonProperty("expected_output")
        private String expectedOutput;

        @JsonProperty("is_hidden")
        private boolean isHidden;

        public Long getTestCaseId() { return testCaseId; }
        public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }

        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }

        public String getActualOutput() { return actualOutput; }
        public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }

        public String getExpectedOutput() { return expectedOutput; }
        public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

        public boolean getIsHidden() { return isHidden; }
        public void setIsHidden(boolean isHidden) { this.isHidden = isHidden; }
    }
}
