package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.UserMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    Page<UserMessage> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
