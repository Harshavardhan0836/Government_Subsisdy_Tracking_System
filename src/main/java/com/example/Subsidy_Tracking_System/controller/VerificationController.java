package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.Verification;
import com.example.Subsidy_Tracking_System.entity.VerificationStatus;
import com.example.Subsidy_Tracking_System.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/Verification/start/{applicationId}")
    public ResponseEntity<Verification> startVerification(@PathVariable Long applicationId) {
        Verification verification = verificationService.startVerification(applicationId);
        return verification != null ? ResponseEntity.ok(verification) : ResponseEntity.notFound().build();
    }

    @PutMapping("/Verification/{id}/advance")
    public ResponseEntity<Verification> advanceStage(@PathVariable Long id,
                                                     @RequestParam VerificationStatus decision,
                                                     @RequestParam(required = false) String remarks) {
        Verification verification = verificationService.advanceStage(id, decision, remarks);
        return verification != null ? ResponseEntity.ok(verification) : ResponseEntity.notFound().build();
    }

    @GetMapping("/Verification/{id}")
    public ResponseEntity<Verification> getVerification(@PathVariable Long id) {
        Verification verification = verificationService.getVerificationById(id);
        return verification != null ? ResponseEntity.ok(verification) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/Verification/{id}")
    public ResponseEntity<Void> deleteVerification(@PathVariable Long id) {
        boolean deleted = verificationService.deleteVerification(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/Verification/count/{status}")
    public Long countByStatus(@PathVariable VerificationStatus status) {
        return verificationService.countByStatus(status);
    }
    @GetMapping("/Verification/turnaround-time")
    public Double getAverageTurnaroundTime() {
        return verificationService.getAverageTurnaroundTime();
    }
}