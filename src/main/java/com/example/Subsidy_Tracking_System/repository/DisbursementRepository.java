package com.example.Subsidy_Tracking_System.repository;

import com.example.Subsidy_Tracking_System.entity.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {
    List<Disbursement> findByApplicationId(Long applicationId);
    @Query("SELECT SUM(d.amount) FROM Disbursement d WHERE d.application.scheme.id = :schemeId AND d.status = 'DISBURSED'")
    Double getTotalDisbursedForScheme(@Param("schemeId") Long schemeId);
    @Query("SELECT d FROM Disbursement d WHERE d.dueDate < CURRENT_DATE AND d.status != 'DISBURSED'")
    List<Disbursement> findOverdueDisbursements();
    @Query("SELECT d.application.beneficiary.region, SUM(d.amount) FROM Disbursement d WHERE d.status = 'DISBURSED' GROUP BY d.application.beneficiary.region")
    List<Object[]> getTotalDisbursedByRegion();
    @Query("SELECT d.application.scheme.id, SUM(d.amount) FROM Disbursement d WHERE d.status = 'DISBURSED' GROUP BY d.application.scheme.id")
    List<Object[]> getTotalDisbursedGroupedByScheme();
}