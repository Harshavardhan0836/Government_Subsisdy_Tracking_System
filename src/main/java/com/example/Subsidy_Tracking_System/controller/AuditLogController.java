package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.AuditLog;
import com.example.Subsidy_Tracking_System.repository.AuditLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/AuditLog")
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}