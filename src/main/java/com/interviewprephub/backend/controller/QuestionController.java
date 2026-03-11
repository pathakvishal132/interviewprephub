package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

        @Autowired
        private QuestionService questionService;

        @GetMapping("/get-question")
        public ResponseEntity<Map<String, Object>> getQuestions(
                        @RequestParam(value = "domain") String domain,
                        @RequestParam(value = "subdomain") String subdomain,
                        @RequestParam(value = "userId", required = false) String userId) {
                return ResponseEntity.ok(
                                questionService.generateQuestions(domain, subdomain, userId));
        }
        // @GetMapping("/get-question")
        // public ResponseEntity<Map<String, Object>> getQuestions(
        // @RequestParam(value = "domain") String domain,
        // @RequestParam(value = "subdomain") String subdomain,
        // @RequestParam(value = "userId", required = false) String userId) {

        // boolean isAuthenticated = userId != null && !userId.isBlank();

        // List<Map<String, String>> questionsList = new ArrayList<>();

        // for (int i = 1; i <= 10; i++) {
        // Map<String, String> q = new HashMap<>();
        // q.put("question", "Sample interview question " + i + " for " + subdomain);
        // questionsList.add(q);
        // }

        // Map<String, Object> response = new HashMap<>();
        // response.put("questions", questionsList);
        // response.put("requiresAuth", !isAuthenticated);

        // if (!isAuthenticated) {
        // response.put("message", "Sign in to access more features and track your
        // progress!");
        // }

        // return ResponseEntity.ok(response);
        // }

        @PostMapping("/feedback")
        public ResponseEntity<Map<String, Object>> getFeedback(
                        @RequestBody Map<String, String> body) {
                String userId = body.get("userId");
                String domain = body.get("domain");
                String subdomain = body.get("subdomain");

                if (userId == null || userId.isBlank()) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("requiresAuth", true);
                        response.put("message", "Sign in to get personalized feedback and track your progress!");
                        return ResponseEntity.ok(response);
                }
                return ResponseEntity.ok(questionService.generateFeedback(
                                body.get("question"), body.get("answer"), userId, domain, subdomain));

        }

        @GetMapping("/submissions")
        public ResponseEntity<Map<String, Object>> getUserSubmissions(
                        @RequestParam(value = "userId") String userId) {
                return ResponseEntity.ok(
                                questionService.getUserSubmissionData(userId));
        }

        @GetMapping("/topics")
        public ResponseEntity<Map<String, Object>> getUserTopics(
                        @RequestParam(value = "userId") String userId) {
                return ResponseEntity.ok(
                                questionService.getUserTopicProgress(userId));
        }
}
