package com.interviewprephub.backend.controller;

import com.interviewprephub.backend.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/emails")
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getMessages(
            @RequestParam(value = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(userMessageService.getMessages(page));
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> submitMessage(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String message = request.get("message");

        return ResponseEntity.ok(userMessageService.saveMessage(name, email, message));
    }

    @DeleteMapping("/delete/emails/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        userMessageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}
