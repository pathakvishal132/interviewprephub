package com.interviewprephub.backend.service;

import java.util.Map;

public interface UserMessageService {
    Map<String, Object> getMessages(int page);
    Map<String, Object> saveMessage(String name, String email, String message);
    void deleteMessage(Long id);
}
