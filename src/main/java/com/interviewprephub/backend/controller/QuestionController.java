package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.QuestionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/get-question")
    public ResponseEntity<Map<String, String>> getQuestions(
            @RequestParam String domain,
            @RequestParam String subdomain
    ) {
        return ResponseEntity.ok(
                questionService.generateQuestions(domain, subdomain)
        );
    }

    @PostMapping("/feedback")
    public ResponseEntity<Map<String, Object>> getFeedback(
            @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(questionService.generateFeedback(body.get("question"),body.get("answer"),body.get("userId")));


    }

    @GetMapping("/submissions")
    public ResponseEntity<Map<String, Object>> getUserSubmissions(
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(
                questionService.getUserSubmissionData(userId)
        );
    }
}
