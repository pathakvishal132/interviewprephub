package com.interviewprephub.backend.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "company_companyquestion")
public class CompanyQuestion {

	public enum Level {
	    High,
	    Medium,
	    Low
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "company_companyquestion_companies",
        joinColumns = @JoinColumn(name = "companyquestion_id"),
        inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies;


    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Level level = Level.Medium;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "date_of_creation", updatable = false)
    private LocalDateTime dateOfCreation;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String role;

    @Column(name = "min_experience")
    private int minExperience = 0;

    @Column(name = "max_experience")
    private int maxExperience = 5;

    @PrePersist
    protected void onCreate() {
        this.dateOfCreation = LocalDateTime.now();
    }

    // -------- getters & setters --------

    public Long getId() {
        return id;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
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

    @Override
    public String toString() {
        return question.length() > 50
                ? question.substring(0, 50) + "..."
                : question;
    }
}

