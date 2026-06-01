package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.AiRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiRequestLogRepository
        extends JpaRepository<AiRequestLog, Long> {

    List<AiRequestLog> findByUserIdOrderByCreatedAtDesc(String userId);

    List<AiRequestLog> findByTypeOrderByCreatedAtDesc(String type);
}
