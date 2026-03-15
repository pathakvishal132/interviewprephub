package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.entity.SubmissionTracker;
import com.interviewprephub.backend.entity.TopicProgress;
import com.interviewprephub.backend.repository.SubmissionTrackerRepository;
import com.interviewprephub.backend.repository.TopicProgressRepository;
import com.interviewprephub.backend.service.QuestionService;
import com.interviewprephub.backend.util.GeminiParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private SubmissionTrackerRepository trackerRepository;

    @Autowired
    private TopicProgressRepository topicProgressRepository;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final int FREE_QUESTION_LIMIT = 2;

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1/models/"
            + "gemini-2.5-flash:generateContent?key=%s";
    // ---------------- QUESTIONS ----------------

    @Override
    public Map<String, Object> generateQuestions(String domain, String subdomain, String userId) {
        boolean isAuthenticated = userId != null && !userId.isBlank();

        int limit = 10;

        String prompt = "You are a senior " + domain + " interview expert.\n" +
                "Generate exactly " + limit + " interview questions for '" + subdomain + "'.\n\n" +
                "it should be always in theoritical perspetive.\n" +
                "Return ONLY valid JSON in the following format:\n" +
                "{\n" +
                "  \"questions\": [\n" +
                "    { \"question\": \"Question text\" }\n" +
                "  ]\n" +
                "}\n\n" +
                "Rules:\n" +
                "- Do NOT use markdown\n" +
                "- Do NOT add explanations\n" +
                "- Do NOT add extra fields\n" +
                "- Output JSON only";
        String responseText = callGemini(prompt);
        Map<String, String> questionsMap = GeminiParser.parseQuestions(responseText);

        // Convert Map<String, String> to List<Map<String, String>>
        List<Map<String, String>> questionsList = new ArrayList<>();
        for (int i = 1; i <= limit; i++) {
            String key = "q" + i;
            if (questionsMap.containsKey(key)) {
                Map<String, String> q = new HashMap<>();
                q.put("question", questionsMap.get(key));
                questionsList.add(q);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questionsList);
        response.put("requiresAuth", !isAuthenticated);

        if (!isAuthenticated) {
            response.put("message", "Sign in to access more features and track your progress!");
        }

        return response;
    }

    // ---------------- FEEDBACK ----------------

    @Override
    public Map<String, Object> generateFeedback(String question, String answer, String userId, String domain,
            String subdomain) {
        LocalDate today = LocalDate.now();
        SubmissionTracker tracker = trackerRepository
                .findByUserIdAndDate(userId, today)
                .orElseGet(() -> {
                    SubmissionTracker t = new SubmissionTracker();
                    t.setUserId(userId);
                    t.setDate(today);
                    t.setSubmissionCount(0);
                    return t;
                });
        tracker.setSubmissionCount(tracker.getSubmissionCount() + 1);
        trackerRepository.save(tracker);

        // Track topic progress
        if (domain != null && subdomain != null) {
            TopicProgress topicProgress = topicProgressRepository
                    .findByUserIdAndDomainAndSubdomain(userId, domain, subdomain)
                    .orElseGet(() -> {
                        TopicProgress tp = new TopicProgress();
                        tp.setUserId(userId);
                        tp.setDomain(domain);
                        tp.setSubdomain(subdomain);
                        tp.setQuestionsAttempted(0);
                        tp.setQuestionsSolved(0);
                        tp.setTotalSubmissions(0);
                        return tp;
                    });
            topicProgress.setQuestionsAttempted(topicProgress.getQuestionsAttempted() + 1);
            topicProgress.setTotalSubmissions(topicProgress.getTotalSubmissions() + 1);
            topicProgress.setLastAttemptDate(today);
            topicProgressRepository.save(topicProgress);
        }

        String prompt = "Provide feedback and the correct answer for: \"" + answer + "\" " +
                "to the question \"" + question + "\". " +
                "Return valid JSON: {\"feedback\":\"...\",\"actualanswer\":\"...\"}";
        String modelResponse = callGemini(prompt);
        modelResponse = modelResponse
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
        Map<String, String> parsed = GeminiParser.parseFeedback(modelResponse);
        Map<String, Object> result = new HashMap<>();
        result.put("feedback",
                StringUtils.hasText(parsed.get("feedback"))
                        ? parsed.get("feedback")
                        : "Our free quota has been exhausted. Can't provide feedback now.");
        result.put("actualAnswer",
                StringUtils.hasText(parsed.get("actualAnswer"))
                        ? parsed.get("actualAnswer")
                        : "Our free quota has been exhausted. Can't provide the correct answer now.");
        result.put("submissionCount", tracker.getSubmissionCount());
        return result;
    }

    // ---------------- TOPIC PROGRESS ----------------

    @Override
    public Map<String, Object> getUserTopicProgress(String userId) {
        List<TopicProgress> topics = topicProgressRepository.findByUserIdOrderByLastAttemptDateDesc(userId);

        Map<String, Object> result = new HashMap<>();

        // Group by domain
        Map<String, List<TopicProgress>> byDomain = new HashMap<>();
        for (TopicProgress tp : topics) {
            byDomain.computeIfAbsent(tp.getDomain(), k -> new ArrayList<>()).add(tp);
        }

        List<Map<String, Object>> domainList = new ArrayList<>();
        for (Map.Entry<String, List<TopicProgress>> entry : byDomain.entrySet()) {
            Map<String, Object> domainMap = new HashMap<>();
            domainMap.put("domain", entry.getKey());

            int totalAttempts = 0;
            int totalSubmissions = 0;
            List<Map<String, Object>> subdomains = new ArrayList<>();

            for (TopicProgress tp : entry.getValue()) {
                Map<String, Object> subdomainMap = new HashMap<>();
                subdomainMap.put("subdomain", tp.getSubdomain());
                subdomainMap.put("questionsAttempted", tp.getQuestionsAttempted());
                subdomainMap.put("totalSubmissions", tp.getTotalSubmissions());
                subdomainMap.put("lastAttempt",
                        tp.getLastAttemptDate() != null ? tp.getLastAttemptDate().toString() : null);
                subdomains.add(subdomainMap);

                totalAttempts += tp.getQuestionsAttempted();
                totalSubmissions += tp.getTotalSubmissions();
            }

            domainMap.put("totalAttempts", totalAttempts);
            domainMap.put("totalSubmissions", totalSubmissions);
            domainMap.put("topics", subdomains);
            domainList.add(domainMap);
        }

        result.put("domains", domainList);
        result.put("totalTopics", topics.size());
        result.put("totalSubmissions", topics.stream().mapToInt(TopicProgress::getTotalSubmissions).sum());

        return result;
    }

    // ---------------- SUBMISSION DATA ----------------

    @Override
    public Map<String, Object> getUserSubmissionData(String userId) {

        List<SubmissionTracker> data = trackerRepository.findByUserIdOrderByDate(userId);

        Map<String, Object> result = new HashMap<>();
        result.put(
                "dates",
                data.stream()
                        .map(e -> e.getDate().toString())
                        .toList());
        result.put(
                "submission_counts",
                data.stream()
                        .map(SubmissionTracker::getSubmissionCount)
                        .toList());

        return result;
    }

    // ---------------- GEMINI REST CALL ----------------

    private String callGemini(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Gemini API key is missing");
        }
        String url = String.format(GEMINI_URL, apiKey);
        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            String text = extractText(response.getBody());
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Error calling Gemini REST API", e);
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> response) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            return parts.get(0).get("text").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }

}
