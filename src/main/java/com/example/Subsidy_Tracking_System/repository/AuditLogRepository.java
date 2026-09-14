package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}