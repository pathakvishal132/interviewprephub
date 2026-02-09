package com.interviewprephub.backend.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "company_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    // -------- getters & setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -------- toString --------
    @Override
    public String toString() {
        return name;
    }
}

