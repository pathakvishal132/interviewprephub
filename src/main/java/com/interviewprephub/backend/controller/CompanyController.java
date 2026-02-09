package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.interviewprephub.backend.entity.Company;

import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCompanies(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.getAllCompanies(page)
        );
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(
            @RequestBody Company company
    ) {
        return ResponseEntity.ok(
                companyService.createCompany(company)
        );
    }

    @GetMapping("/questions/{companyId}")
    public ResponseEntity<Map<String, Object>> getCompanyQuestions(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.getQuestionsByCompany(companyId, page)
        );
    }

    @GetMapping("/search-question")
    public ResponseEntity<Map<String, Object>> searchQuestions(
            @RequestParam String word,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.searchQuestions(word, page)
        );
    }

    @GetMapping("/reviews")
    public ResponseEntity<Map<String, Object>> getCompanyReviews(
            @RequestParam String companyId,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.getCompanyReviews(companyId, page)
        );
    }
}
