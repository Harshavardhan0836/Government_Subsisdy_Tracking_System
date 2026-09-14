package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.Application;
import com.example.Subsidy_Tracking_System.entity.Beneficiary;
import com.example.Subsidy_Tracking_System.entity.Scheme;
import com.example.Subsidy_Tracking_System.repository.ApplicationRepository;
import com.example.Subsidy_Tracking_System.repository.BeneficiaryRepository;
import com.example.Subsidy_Tracking_System.repository.SchemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final BeneficiaryRepository beneficiaryRepository;
    private final SchemeRepository schemeRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              BeneficiaryRepository beneficiaryRepository,
                              SchemeRepository schemeRepository) {
        this.applicationRepository = applicationRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.schemeRepository = schemeRepository;
    }

    public Application registerApplication(Application application) {
        Long beneficiaryId = application.getBeneficiary().getId();
        Optional<Beneficiary> beneficiaryResult = beneficiaryRepository.findById(beneficiaryId);

        Long schemeId = application.getScheme().getId();
        Optional<Scheme> schemeResult = schemeRepository.findById(schemeId);

        if (beneficiaryResult.isPresent() && schemeResult.isPresent()) {
            application.setBeneficiary(beneficiaryResult.get());
            application.setScheme(schemeResult.get());
            return applicationRepository.save(application);
        } else {
            return null;
        }
    }
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }
    public Application getApplicationById(Long id) {
        Optional<Application> result = applicationRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }
    public Application updateApplicationById(Long id, Application updatedApplication) {
        Optional<Application> result = applicationRepository.findById(id);
        if (result.isPresent()) {
            Application existing = result.get();
            existing.setStatus(updatedApplication.getStatus());
            existing.setApplicationDate(updatedApplication.getApplicationDate());

            // same "fetch the real object" logic as create, in case beneficiary/scheme is being changed
            Beneficiary beneficiary = beneficiaryRepository.findById(updatedApplication.getBeneficiary().getId()).orElse(null);
            Scheme scheme = schemeRepository.findById(updatedApplication.getScheme().getId()).orElse(null);
            existing.setBeneficiary(beneficiary);
            existing.setScheme(scheme);

            return applicationRepository.save(existing);
        } else {
            return null;
        }
    }
    public boolean deleteApplication(Long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}