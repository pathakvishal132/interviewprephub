package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.interviewprephub.backend.entity.Company;
import com.interviewprephub.backend.entity.CompanyQuestion.Level;
import com.interviewprephub.backend.repository.CompanyReviewRepository;
import com.interviewprephub.backend.entity.CompanyReview;

import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyReviewRepository reviewRepository;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCompanies(
    		@RequestParam(required = false) String searchText,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.getAllCompanies(searchText , page)
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
    
    @PostMapping("/filter")
    public ResponseEntity<?> filterQuestions(
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String role,
            @RequestParam(name = "min_experience", required = false) Integer minExperience,
            @RequestParam(name = "max_experience", required = false) Integer maxExperience,
            @RequestParam(required = false) String description,
            @RequestParam(required = false ) String searchText,
            @RequestParam(defaultValue = "1") int page
    ) {
    	Map<String, Object> result = companyService.getFilteredQuestion(level , role , minExperience , maxExperience , description , searchText ,page);
    	return ResponseEntity.ok(result);
    }
    
    @GetMapping("/get-other-details/{companyId}")
    public ResponseEntity<Map<String , Object>> getOtherDetails(
    		@PathVariable Long companyId)
    {
    	Map<String, Object> result = companyService.getOtherDetails(companyId);
    	
    	return ResponseEntity.ok(result);
    }


    @GetMapping("/reviews")
    public ResponseEntity<?> getCompanyReviews(
            @RequestParam String company_id,
            @RequestParam(defaultValue = "1") int page
    ) {
        try {
            Map<String, Object> result =
            		companyService.getCompanyReviews(company_id, page);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("detail", e.getMessage()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("detail", e.getMessage()));
        }
    }
    
    @PostMapping("/reviews")
    public ResponseEntity<?> postCompanyReview(
            @RequestBody CompanyReview review
    ) {
        try {
        	System.out.println(review);
            if (review.getCompanyId() == null || review.getCompanyId().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("detail", "Company ID is required."));
            }
            CompanyReview savedReview = reviewRepository.save(review);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedReview);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("detail", e.getMessage()));
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }
}
