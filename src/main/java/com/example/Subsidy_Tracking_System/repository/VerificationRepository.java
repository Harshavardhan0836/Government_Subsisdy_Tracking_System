package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Subsidy_Tracking_System.entity.VerificationStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface VerificationRepository extends JpaRepository<Verification, Long> {
    @Query("SELECT COUNT(v) FROM Verification v WHERE v.status = :status")
    Long countByStatus(@Param("status") VerificationStatus status);
    List<Verification> findByCompletedDateIsNotNull();
}