package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.Disbursement;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ReminderJob {

    private final DisbursementService disbursementService;

    public ReminderJob(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    @Scheduled(fixedRate = 60000)
    public void checkOverdueDisbursements() {
        List<Disbursement> overdue = disbursementService.getOverdueDisbursements();
        if (!overdue.isEmpty()) {
            System.out.println("REMINDER: " + overdue.size() + " disbursement(s) are overdue!");
            for (Disbursement d : overdue) {
                System.out.println(" - Disbursement ID " + d.getId() + ", due " + d.getDueDate());
            }
        }
    }
}