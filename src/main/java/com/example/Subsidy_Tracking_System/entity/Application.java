package com.example.Subsidy_Tracking_System.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="application")
@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate applicationDate;
    private String status;
    @ManyToOne
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;
    @ManyToOne
    @JoinColumn(name = "scheme_id")
    private Scheme scheme;
}
