package com.interviewprephub.backend.serviceImpl;

import com.interviewprephub.backend.entity.UserMessage;
import com.interviewprephub.backend.repository.UserMessageRepository;
import com.interviewprephub.backend.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageRepository userMessageRepository;

    @Override
    public Map<String, Object> getMessages(int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<UserMessage> messagePage = userMessageRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<Map<String, Object>> messages = messagePage.getContent().stream()
                .map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", msg.getId());
                    map.put("name", msg.getName());
                    map.put("email", msg.getEmail());
                    map.put("message", msg.getMessage());
                    map.put("created_at", msg.getCreatedAt());
                    return map;
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        response.put("current_page", page);
        response.put("total_pages", messagePage.getTotalPages());
        response.put("total_messages", messagePage.getTotalElements());

        return response;
    }

    @Override
    public Map<String, Object> saveMessage(String name, String email, String message) {
        UserMessage userMessage = new UserMessage();
        userMessage.setName(name);
        userMessage.setEmail(email);
        userMessage.setMessage(message);
        userMessage.setCreatedAt(LocalDateTime.now());

        UserMessage saved = userMessageRepository.save(userMessage);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("message", "Message sent successfully!");

        return response;
    }

    @Override
    public void deleteMessage(Long id) {
        userMessageRepository.deleteById(id);
    }
}
