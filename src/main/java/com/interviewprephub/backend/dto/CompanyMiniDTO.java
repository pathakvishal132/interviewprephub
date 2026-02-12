package com.interviewprephub.backend.dto;

public class CompanyMiniDTO {

    private Long id;
    private String name;

    public CompanyMiniDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
