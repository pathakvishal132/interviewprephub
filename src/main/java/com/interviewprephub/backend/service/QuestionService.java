package com.interviewprephub.backend.service;

import java.util.Map;

public interface QuestionService {
	
	Map<String, String> generateQuestions(String domain, String subdomain);

    Map<String, Object> generateFeedback(
            String question,
            String answer,
            String userId
    );

    Map<String, Object> getUserSubmissionData(String userId);
}
