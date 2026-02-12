package com.interviewprephub.backend.service;
import com.interviewprephub.backend.entity.Company;
import java.util.Map;

public interface CompanyService {

    Map<String, Object> getQuestionsByCompany(Long companyId, int page);

    Map<String, Object> getCompanyReviews(String companyId, int page);

    Object postCompanyReview(Object body);
    
    Map<String, Object> getAllCompanies(String searchText , int page);

    Company createCompany(Company company);
    
    Map<String , Object> getOtherDetails(Long companyId);

	Map<String, Object> getFilteredQuestion(String level, String role, Integer minExperience, Integer maxExperience,
			String description, String searchText , int page);
}
