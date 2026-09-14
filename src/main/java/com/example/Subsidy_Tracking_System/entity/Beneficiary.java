package com.example.Subsidy_Tracking_System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "beneficiaries")
@Data
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private Integer age;

    private String gender;

    private Double income;

    private String occupation;

    private String category;

    private String contactNumber;

    private String address;

    private String identityNumber;

    private boolean identityVerified = false;
    private String identityDocumentPath;
    private String region;
}