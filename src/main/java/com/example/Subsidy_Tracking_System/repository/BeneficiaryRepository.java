package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    @Query("SELECT b.category, COUNT(b) FROM Beneficiary b GROUP BY b.category")
    List<Object[]> getBeneficiaryCountByCategory();
}