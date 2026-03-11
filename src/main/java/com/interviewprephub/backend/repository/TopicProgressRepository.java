package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.TopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicProgressRepository extends JpaRepository<TopicProgress, Long> {
    List<TopicProgress> findByUserIdOrderByLastAttemptDateDesc(String userId);
    
    Optional<TopicProgress> findByUserIdAndDomainAndSubdomain(String userId, String domain, String subdomain);
    
    List<TopicProgress> findByUserId(String userId);
}
