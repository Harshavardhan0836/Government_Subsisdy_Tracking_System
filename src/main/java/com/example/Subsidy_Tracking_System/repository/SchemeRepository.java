package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemeRepository extends JpaRepository<Scheme, Long> {
}
