package com.example.Subsidy_Tracking_System.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "verification")
@Data
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id")
    private Application application;

    private Integer eligibilityScore;

    @Enumerated(EnumType.STRING)
    private VerificationStage currentStage;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private String remarks;
    private LocalDate completedDate;
}