package com.example.Subsidy_Tracking_System.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "disbursement")
@Data
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    private Integer stageNumber;

    private Double amount;

    private String milestoneDescription;

    @Enumerated(EnumType.STRING)
    private DisbursementStatus status;

    private LocalDate disbursedDate;
    private LocalDate dueDate;
}