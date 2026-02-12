package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.CompanyReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyReviewRepository extends JpaRepository<CompanyReview, Long> {

    Page<CompanyReview> findByCompanyId(String companyId, Pageable pageable);
   

}
