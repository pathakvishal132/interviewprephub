package com.interviewprephub.backend.service;

import java.util.Map;

public interface QuestionService {
	
	Map<String, Object> generateQuestions(String domain, String subdomain, String userId);

    Map<String, Object> generateFeedback(
            String question,
            String answer,
            String userId,
            String domain,
            String subdomain
    );

    Map<String, Object> getUserSubmissionData(String userId);
    
    Map<String, Object> getUserTopicProgress(String userId);
}
