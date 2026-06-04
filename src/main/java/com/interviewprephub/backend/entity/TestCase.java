package com.interviewprephub.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coding_question_test_case")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_question_id", nullable = false)
    private CodingQuestion codingQuestion;

    @Column(name = "input_data", nullable = false, columnDefinition = "TEXT")
    private String inputData;

    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "is_hidden")
    private Boolean isHidden = false;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(length = 500)
    private String explanation;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CodingQuestion getCodingQuestion() { return codingQuestion; }
    public void setCodingQuestion(CodingQuestion codingQuestion) { this.codingQuestion = codingQuestion; }

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
