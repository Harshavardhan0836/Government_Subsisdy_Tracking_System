package com.example.Subsidy_Tracking_System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="scheme")
@Data
public class Scheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private boolean active = true;
    private String schemeName;
    private String category;
    private double grantAmount;
    private String region;
    private String minEligibleCriteria;
    private Double maxIncomeLimit;
    private Integer incomePoints;
    private Integer minAge;
    private Integer maxAge;
    private Integer agePoints;
    private Double totalBudget;
}
