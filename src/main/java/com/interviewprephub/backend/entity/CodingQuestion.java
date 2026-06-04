package com.interviewprephub.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "company_coding_question")
public class CodingQuestion {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @ManyToMany
    @JoinTable(
        name = "company_coding_question_companies",
        joinColumns = @JoinColumn(name = "coding_question_id"),
        inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies;

    @Column(name = "starter_code_java", columnDefinition = "TEXT")
    private String starterCodeJava;

    @Column(name = "starter_code_python", columnDefinition = "TEXT")
    private String starterCodePython;

    @Column(name = "starter_code_cpp", columnDefinition = "TEXT")
    private String starterCodeCpp;

    @Column(name = "starter_code_javascript", columnDefinition = "TEXT")
    private String starterCodeJavascript;

    @Column(name = "date_of_creation", updatable = false)
    private LocalDateTime dateOfCreation;

    @PrePersist
    protected void onCreate() {
        this.dateOfCreation = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public Set<Company> getCompanies() { return companies; }
    public void setCompanies(Set<Company> companies) { this.companies = companies; }

    public String getStarterCodeJava() { return starterCodeJava; }
    public void setStarterCodeJava(String starterCodeJava) { this.starterCodeJava = starterCodeJava; }

    public String getStarterCodePython() { return starterCodePython; }
    public void setStarterCodePython(String starterCodePython) { this.starterCodePython = starterCodePython; }

    public String getStarterCodeCpp() { return starterCodeCpp; }
    public void setStarterCodeCpp(String starterCodeCpp) { this.starterCodeCpp = starterCodeCpp; }

    public String getStarterCodeJavascript() { return starterCodeJavascript; }
    public void setStarterCodeJavascript(String starterCodeJavascript) { this.starterCodeJavascript = starterCodeJavascript; }

    public LocalDateTime getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(LocalDateTime dateOfCreation) { this.dateOfCreation = dateOfCreation; }
}
