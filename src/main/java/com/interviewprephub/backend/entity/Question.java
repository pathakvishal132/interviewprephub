package com.interviewprephub.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String question;

    @Column(length = 200, nullable = false)
    private String domain;

    @Column(length = 200)
    private String subdomain = "";

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;
    public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}

