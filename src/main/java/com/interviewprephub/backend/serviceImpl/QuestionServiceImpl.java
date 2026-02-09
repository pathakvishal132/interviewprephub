package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.entity.SubmissionTracker;
import com.interviewprephub.backend.repository.SubmissionTrackerRepository;
import com.interviewprephub.backend.service.QuestionService;
import com.interviewprephub.backend.util.GeminiParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private SubmissionTrackerRepository trackerRepository;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_URL =
    	    "https://generativelanguage.googleapis.com/v1/models/"
    	  + "gemini-2.5-flash:generateContent?key=%s";



    // ---------------- QUESTIONS ----------------

    @Override
    public Map<String, String> generateQuestions(String domain, String subdomain) {

    	String prompt =
    		    "You are a senior " + domain + " interview expert.\n" +
    		    "Generate exactly 10 interview questions for '" + subdomain + "'.\n\n" +
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

        return GeminiParser.parseQuestions(responseText);
    }

    // ---------------- FEEDBACK ----------------

    @Override
    public Map<String, Object> generateFeedback(
            String question,
            String answer,
            String userId
    ) {

        LocalDate today = LocalDate.now();

        SubmissionTracker tracker =
                trackerRepository
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

        String prompt =
                "Provide feedback and the correct answer for: \"" + answer + "\" " +
                "to the question \"" + question + "\". " +
                "Return valid JSON.";

        String modelResponse = callGemini(prompt);

        Map<String, String> parsed =
                GeminiParser.parseFeedback(modelResponse);

        Map<String, Object> result = new HashMap<>();
        result.put("feedback", parsed.get("feedback"));
        result.put("actualAnswer", parsed.get("actualAnswer"));
        result.put("submissionCount", tracker.getSubmissionCount());

        return result;
    }

    // ---------------- SUBMISSION DATA ----------------

    @Override
    public Map<String, Object> getUserSubmissionData(String userId) {

        List<SubmissionTracker> data =
                trackerRepository.findByUserIdOrderByDate(userId);

        Map<String, Object> result = new HashMap<>();
        result.put(
                "dates",
                data.stream()
                    .map(e -> e.getDate().toString())
                    .toList()
        );
        result.put(
                "submission_counts",
                data.stream()
                    .map(SubmissionTracker::getSubmissionCount)
                    .toList()
        );

        return result;
    }

    // ---------------- GEMINI REST CALL ----------------

    private String callGemini(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Gemini API key is missing");
        }

        String url = String.format(GEMINI_URL, apiKey);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(url, request, Map.class);

            // 🔴 ADD THESE LINES HERE
            String text = extractText(response.getBody());

            System.out.println("===== RAW GEMINI RESPONSE =====");
            System.out.println(text);
            System.out.println("================================");

            return text;

        } catch (Exception e) {
            throw new RuntimeException("Error calling Gemini REST API", e);
        }
    }


    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> response) {

        try {
            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            return parts.get(0).get("text").toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }
    }
}
