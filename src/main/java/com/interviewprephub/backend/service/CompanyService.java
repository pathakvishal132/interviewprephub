package com.interviewprephub.backend.service;
import com.interviewprephub.backend.entity.Company;
import java.util.Map;

public interface CompanyService {

    Map<String, Object> createCompanyQuestion(Object request);

    Map<String, Object> getAllCompanyQuestions(int page);

    Map<String, Object> getQuestionsByCompany(Long companyId, int page);

    Map<String, Object> searchCompanies(String word, int page);

    Map<String, Object> filterCompanyQuestions(Map<String, Object> filters, int page);

    Map<String, Object> searchQuestions(String word, int page);

    Map<String, Object> getCompanyReviews(String companyId, int page);

    Object postCompanyReview(Object body);
    
    Map<String, Object> getAllCompanies(int page);

    Company createCompany(Company company);
}
