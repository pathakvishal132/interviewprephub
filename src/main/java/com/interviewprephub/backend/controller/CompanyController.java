package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.interviewprephub.backend.entity.Company;
import com.interviewprephub.backend.entity.CompanyQuestion.Level;

import java.util.Map;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    
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

//    @GetMapping("/search-question")
//    public ResponseEntity<Map<String, Object>> searchQuestions(
//            @RequestParam String word,
//            @RequestParam(defaultValue = "1") int page
//    ) {
//        return ResponseEntity.ok(
//                companyService.searchQuestions(word, page)
//        );
//    }
    
    @GetMapping("/get-other-details/{companyId}")
    public ResponseEntity<Map<String , Object>> getOtherDetails(
    		@PathVariable Long companyId)
    {
    	Map<String, Object> result = companyService.getOtherDetails(companyId);
    	
    	return ResponseEntity.ok(result);
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


    @GetMapping("/reviews")
    public ResponseEntity<Map<String, Object>> getCompanyReviews(
            @RequestParam String companyId,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(
                companyService.getCompanyReviews(companyId, page)
        );
    }
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }
}
