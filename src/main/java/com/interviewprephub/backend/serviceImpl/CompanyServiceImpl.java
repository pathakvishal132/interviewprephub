package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.dto.CompanyDTO;
import com.interviewprephub.backend.dto.CompanyQuestionDTO;
import com.interviewprephub.backend.entity.Company;
import com.interviewprephub.backend.entity.CompanyQuestion;
import com.interviewprephub.backend.entity.CompanyReview;
import com.interviewprephub.backend.mapper.CompanyMapper;
import com.interviewprephub.backend.mapper.CompanyQuestionMapper;
import com.interviewprephub.backend.repository.CompanyQuestionRepository;
import com.interviewprephub.backend.repository.CompanyRepository;
import com.interviewprephub.backend.repository.CompanyReviewRepository;
import com.interviewprephub.backend.service.CompanyService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyQuestionRepository questionRepository;

    @Autowired
    private CompanyReviewRepository reviewRepository;

    private static final int COMPANY_PAGE_SIZE = 8;
    private static final int QUESTION_PAGE_SIZE = 5;
    private static final int REVIEW_PAGE_SIZE = 6;

    // ---------------- COMPANIES ----------------

    @Override
    public Map<String, Object> getAllCompanies(int page) {

        int safePage = Math.max(page, 1);

        Pageable pageable = PageRequest.of(
                safePage - 1,
                COMPANY_PAGE_SIZE,
                Sort.by("id").ascending()
        );

        Page<Company> companyPage = companyRepository.findAll(pageable);

        List<CompanyDTO> companies = companyPage
                .getContent()
                .stream()
                .map(CompanyMapper::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("companies", companies);
        response.put("total_pages", companyPage.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_companies", companyPage.getTotalElements());

        return response;
    }

    @Override
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    // ---------------- QUESTIONS BY COMPANY ----------------
//    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> getQuestionsByCompany(Long companyId, int page) {

        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, QUESTION_PAGE_SIZE);

        Page<CompanyQuestion> questionPage =
                questionRepository.findByCompanies_Id(companyId, pageable);

        List<CompanyQuestionDTO> questions = questionPage
                .getContent()
                .stream()
                .map(CompanyQuestionMapper::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        response.put("total_pages", questionPage.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_questions", questionPage.getTotalElements());

        return response;
    }

    // ---------------- SEARCH QUESTIONS ----------------

    @Override
    public Map<String, Object> searchQuestions(String word, int page) {

        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, QUESTION_PAGE_SIZE);

        Page<CompanyQuestion> result = questionRepository.searchQuestions(word, pageable);

        List<CompanyQuestionDTO> questions = result
                .getContent()
                .stream()
                .map(CompanyQuestionMapper::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        response.put("total_pages", result.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_questions", result.getTotalElements());

        return response;
    }

    // ---------------- COMPANY REVIEWS ----------------

    @Override
    public Map<String, Object> getCompanyReviews(String companyId, int page) {

        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, REVIEW_PAGE_SIZE);

        Page<CompanyReview> reviewPage =
                reviewRepository.findByCompanyId(companyId, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewPage.getContent());
        response.put("total_pages", reviewPage.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_reviews", reviewPage.getTotalElements());

        return response;
    }

    // ---------------- TODO / FUTURE ----------------

    @Override
    public Object postCompanyReview(Object body) {
        // TODO: Accept ReviewDTO → map → save → return DTO
        return body;
    }

    @Override
    public Map<String, Object> createCompanyQuestion(Object request) {
        return null;
    }

    @Override
    public Map<String, Object> getAllCompanyQuestions(int page) {
        return null;
    }

    @Override
    public Map<String, Object> searchCompanies(String word, int page) {
        return null;
    }

    @Override
    public Map<String, Object> filterCompanyQuestions(Map<String, Object> filters, int page) {
        return null;
    }
}
