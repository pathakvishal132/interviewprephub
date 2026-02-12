package com.interviewprephub.backend.repository;

import com.interviewprephub.backend.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
