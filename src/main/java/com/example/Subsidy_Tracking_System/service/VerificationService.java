package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.*;
import com.example.Subsidy_Tracking_System.repository.ApplicationRepository;
import com.example.Subsidy_Tracking_System.repository.VerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationService {

    private final VerificationRepository verificationRepository;
    private final ApplicationRepository applicationRepository;
    private final AuditLogService auditLogService;

    public VerificationService(VerificationRepository verificationRepository,
                               ApplicationRepository applicationRepository,
                               AuditLogService auditLogService) {
        this.verificationRepository = verificationRepository;
        this.applicationRepository = applicationRepository;
        this.auditLogService = auditLogService;
    }

    public Verification startVerification(Long applicationId) {
        Optional<Application> appResult = applicationRepository.findById(applicationId);
        if (appResult.isEmpty()) {
            return null;
        }

        Application application = appResult.get();
        Beneficiary beneficiary = application.getBeneficiary();

        Scheme scheme = application.getScheme();
        int score = calculateEligibilityScore(beneficiary, scheme);

        Verification verification = new Verification();
        verification.setApplication(application);
        verification.setEligibilityScore(score);
        verification.setCurrentStage(VerificationStage.FIELD_OFFICER);
        verification.setStatus(VerificationStatus.PENDING);

        return verificationRepository.save(verification);
    }

    private int calculateEligibilityScore(Beneficiary beneficiary, Scheme scheme) {
        int score = 0;

        if (beneficiary.getIncome() != null && scheme.getMaxIncomeLimit() != null
                && beneficiary.getIncome() < scheme.getMaxIncomeLimit()) {
            score += scheme.getIncomePoints() != null ? scheme.getIncomePoints() : 0;
        }

        if (beneficiary.getAge() != null && scheme.getMinAge() != null && scheme.getMaxAge() != null
                && beneficiary.getAge() >= scheme.getMinAge() && beneficiary.getAge() <= scheme.getMaxAge()) {
            score += scheme.getAgePoints() != null ? scheme.getAgePoints() : 0;
        }

        if (beneficiary.isIdentityVerified()) {
            score += 30;
        }

        return score;
    }

    public Verification advanceStage(Long verificationId, VerificationStatus decision, String remarks) {
        Optional<Verification> result = verificationRepository.findById(verificationId);
        if (result.isEmpty()) {
            return null;
        }

        Verification verification = result.get();
        verification.setRemarks(remarks);

        if (decision == VerificationStatus.REJECTED) {
            verification.setStatus(VerificationStatus.REJECTED);
            auditLogService.log("VERIFICATION_REJECTED",
                    "Verification ID " + verification.getId() + " was rejected. Remarks: " + remarks);
            return verificationRepository.save(verification);
        }

        VerificationStage current = verification.getCurrentStage();

        if (current == VerificationStage.FIELD_OFFICER) {

            verification.setCurrentStage(VerificationStage.DISTRICT_OFFICER);

        } else if (current == VerificationStage.DISTRICT_OFFICER) {

            verification.setCurrentStage(VerificationStage.FINANCE_APPROVAL);

        } else if (current == VerificationStage.FINANCE_APPROVAL) {

            verification.setCurrentStage(VerificationStage.COMPLETED);
            verification.setStatus(VerificationStatus.APPROVED);
            verification.setCompletedDate(java.time.LocalDate.now());
        }

        auditLogService.log(
                "VERIFICATION_ADVANCED",
                "Verification ID " + verification.getId()
                        + " advanced from " + current
                        + " with decision " + decision
                        + ". Remarks: " + remarks
        );

        return verificationRepository.save(verification);
    }

    public Verification getVerificationById(Long id) {
        Optional<Verification> result = verificationRepository.findById(id);
        return result.isPresent() ? result.get() : null;
    }
    public boolean deleteVerification(Long id) {
        if (verificationRepository.existsById(id)) {
            verificationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Long countByStatus(VerificationStatus status) {
        return verificationRepository.countByStatus(status);
    }
    public Double getAverageTurnaroundTime() {
        List<Verification> completed = verificationRepository.findByCompletedDateIsNotNull();

        if (completed.isEmpty()) {
            return 0.0;
        }

        long totalDays = 0;
        int count = 0;

        for (Verification v : completed) {
            LocalDate applicationDate = v.getApplication().getApplicationDate();
            LocalDate completedDate = v.getCompletedDate();

            if (applicationDate != null && completedDate != null) {
                long days = ChronoUnit.DAYS.between(applicationDate, completedDate);
                totalDays += days;
                count++;
            }
        }

        return count > 0 ? (double) totalDays / count : 0.0;
    }
}