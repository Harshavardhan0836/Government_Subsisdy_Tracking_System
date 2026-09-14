package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.Application;
import com.example.Subsidy_Tracking_System.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/Application")
    public ResponseEntity<Application> addApplication(@RequestBody Application application) {
        Application saved = applicationService.registerApplication(application);
        if (saved != null) {
            return ResponseEntity.ok(saved);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/Application")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/Application/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Application application = applicationService.getApplicationById(id);
        if (application != null) {
            return ResponseEntity.ok(application);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/Application/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application updatedApplication) {
        Application application = applicationService.updateApplicationById(id, updatedApplication);
        if (application != null) {
            return ResponseEntity.ok(application);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/Application/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        boolean deleted = applicationService.deleteApplication(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}