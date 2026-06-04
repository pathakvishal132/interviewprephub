package com.interviewprephub.backend.service;

import com.interviewprephub.backend.dto.CreateCodingQuestionRequest;
import com.interviewprephub.backend.dto.SubmitCodeRequest;
import com.interviewprephub.backend.dto.SubmitCodeResponse;
import java.util.Map;

public interface CodingService {

    Map<String, Object> getCodingQuestionsByCompany(Long companyId, int page);

    Map<String, Object> getCodingQuestionDetail(Long questionId);

    SubmitCodeResponse executeCode(Long userId, SubmitCodeRequest request, boolean isSubmission);

    Map<String, Object> createCodingQuestion(CreateCodingQuestionRequest request);

    Map<String, Object> getSubmissionStats(Long userId, int days);

    void deleteCodingQuestion(Long questionId);
}
