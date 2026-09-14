package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
