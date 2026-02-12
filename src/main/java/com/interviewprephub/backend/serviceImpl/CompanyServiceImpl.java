package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.dto.CompanyDTO;
import com.interviewprephub.backend.dto.CompanyMiniDTO;
import com.interviewprephub.backend.dto.CompanyQuestionDTO;
import com.interviewprephub.backend.entity.Company;
import com.interviewprephub.backend.entity.CompanyQuestion;
import com.interviewprephub.backend.entity.CompanyQuestion.Level;
import com.interviewprephub.backend.entity.CompanyReview;
import com.interviewprephub.backend.mapper.CompanyMapper;
import com.interviewprephub.backend.mapper.CompanyQuestionMapper;
import com.interviewprephub.backend.repository.CompanyQuestionRepository;
import com.interviewprephub.backend.repository.CompanyRepository;
import com.interviewprephub.backend.repository.CompanyReviewRepository;
import com.interviewprephub.backend.service.CompanyService;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public Map<String, Object> getAllCompanies(String searchText , int page) {
        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1,COMPANY_PAGE_SIZE,Sort.by("id").ascending());
        Page<Company> companyPage  = null ; 
        if(StringUtils.hasText(searchText)) {
        	companyPage = companyRepository.findByNameContainingIgnoreCase(searchText , pageable);
        }
        else {
        	 companyPage = companyRepository.findAll(pageable);
        }
       
        List<CompanyDTO> companies = companyPage.getContent().stream().map(CompanyMapper::toDTO)
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
    @Override
    public Map<String, Object> getOtherDetails(Long companyId) {
        List<CompanyQuestion> rows =
                questionRepository.findAllByCompanies_Id(companyId);
        if (rows.isEmpty()) {
            throw new RuntimeException("No details found for the given company ID.");
        }
        Set<Level> levels = new HashSet<>();
        Set<String> roles = new HashSet<>();
        Set<Integer> minExperiences = new HashSet<>();
        Set<Integer> maxExperiences = new HashSet<>();
        Set<String> descriptions = new HashSet<>();
        for (CompanyQuestion q : rows) {
            if (q.getLevel() != null)
                levels.add(q.getLevel());
            if (q.getRole() != null)
                roles.add(q.getRole());
            minExperiences.add(q.getMinExperience());
            maxExperiences.add(q.getMaxExperience());
            if (q.getDescription() != null)
                descriptions.add(q.getDescription());
        }
        Map<String, Object> res = new HashMap<>();
        res.put("company_id", companyId);
        res.put("levels", new ArrayList<>(levels));
        res.put("roles", new ArrayList<>(roles));
        res.put("min_experiences", new ArrayList<>(minExperiences));
        res.put("max_experiences", new ArrayList<>(maxExperiences));
        res.put("descriptions", new ArrayList<>(descriptions));
        return res;
    }

    @Override
    public Map<String, Object> getFilteredQuestion( String level,String role,Integer minExperience,Integer maxExperience, String description, String searchText , int page) {
        int safePage = Math.max(page, 1);
        Pageable pageable = PageRequest.of(safePage - 1, QUESTION_PAGE_SIZE);
        Specification<CompanyQuestion> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (level != null && !level.isEmpty()) {
                CompanyQuestion.Level enumLevel =
                        CompanyQuestion.Level.valueOf(level);
                predicates.add(cb.equal(root.get("level"), enumLevel));
            }
            if (role != null && !role.isEmpty()) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            if (minExperience != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("minExperience"), minExperience));	
            }
            if (maxExperience != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("maxExperience"), maxExperience));
            }
            if (description != null && !description.isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(searchText)) {
                predicates.add(cb.like(
                        cb.lower(root.get("question")),
                        "%" + searchText.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<CompanyQuestion> resultPage =
                questionRepository.findAll(spec, pageable);
        List<CompanyQuestionDTO> dtoList = resultPage.getContent()
                .stream()
                .map(q -> {
                    CompanyQuestionDTO dto = new CompanyQuestionDTO();
                    dto.setId(q.getId());
                    dto.setQuestion(q.getQuestion());
                    dto.setAnswer(q.getAnswer());
                    dto.setLevel(q.getLevel().name());
                    dto.setDescription(q.getDescription());
                    dto.setRole(q.getRole());
                    dto.setMinExperience(q.getMinExperience());
                    dto.setMaxExperience(q.getMaxExperience());
                    dto.setDateOfCreation(q.getDateOfCreation());
                    List<CompanyMiniDTO> companyDTOs = q.getCompanies()
                            .stream()
                            .map(c -> new CompanyMiniDTO(c.getId(), c.getName()))
                            .toList();
                    dto.setCompanies(companyDTOs);
                    return dto;
                })
                .toList();
        Map<String, Object> response = new HashMap<>();
        response.put("questions", dtoList);
        response.put("total_pages", resultPage.getTotalPages());
        response.put("current_page", safePage);
        response.put("total_questions", resultPage.getTotalElements());
        return response;
    }
}
