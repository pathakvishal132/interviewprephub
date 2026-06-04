package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.dto.CreateCodingQuestionRequest;
import com.interviewprephub.backend.dto.SubmitCodeRequest;
import com.interviewprephub.backend.dto.SubmitCodeResponse;
import com.interviewprephub.backend.service.CodingService;
import com.interviewprephub.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coding")
public class CodingController {

    @Autowired
    private CodingService codingService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Map<String, Object>> getCodingQuestions(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(
                codingService.getCodingQuestionsByCompany(companyId, page));
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> getCodingQuestionDetail(
            @PathVariable Long questionId) {
        return ResponseEntity.ok(
                codingService.getCodingQuestionDetail(questionId));
    }

    @PostMapping("/run")
    public ResponseEntity<SubmitCodeResponse> runCode(
            @RequestBody SubmitCodeRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        SubmitCodeResponse response = codingService.executeCode(userId, request, false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    public ResponseEntity<?> createCodingQuestion(
            @RequestBody CreateCodingQuestionRequest request,
            HttpServletRequest httpRequest) {
        String email = extractUserEmail(httpRequest);
        if (!"pathakvishal132@gmail.com".equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("detail", "Only the admin can create coding questions"));
        }
        Map<String, Object> result = codingService.createCodingQuestion(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<?> deleteCodingQuestion(
            @PathVariable Long questionId,
            HttpServletRequest httpRequest) {
        String email = extractUserEmail(httpRequest);
        if (!"pathakvishal132@gmail.com".equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("detail", "Only the admin can delete coding questions"));
        }
        codingService.deleteCodingQuestion(questionId);
        return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
    }

    @GetMapping("/submissions/stats")
    public ResponseEntity<Map<String, Object>> getSubmissionStats(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(codingService.getSubmissionStats(userId, Math.min(days, 365)));
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmitCodeResponse> submitCode(
            @RequestBody SubmitCodeRequest request,
            HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        SubmitCodeResponse response = codingService.executeCode(userId, request, true);
        return ResponseEntity.ok(response);
    }

    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                return (long) username.hashCode();
            } catch (Exception e) {
                return 0L;
            }
        }
        return 0L;
    }

    private String extractUserEmail(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                return jwtUtil.extractUsername(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
