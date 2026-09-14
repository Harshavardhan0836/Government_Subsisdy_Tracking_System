package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.Disbursement;
import com.example.Subsidy_Tracking_System.service.DisbursementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class DisbursementController {

    private final DisbursementService disbursementService;

    public DisbursementController(DisbursementService disbursementService) {
        this.disbursementService = disbursementService;
    }

    @PostMapping("/Disbursement/{applicationId}")
    public ResponseEntity<Disbursement> createDisbursement(@PathVariable Long applicationId,
                                                           @RequestBody Disbursement disbursement) {
        Disbursement saved = disbursementService.createDisbursement(applicationId, disbursement);
        return saved != null ? ResponseEntity.ok(saved) : ResponseEntity.notFound().build();
    }

    @GetMapping("/Disbursement/application/{applicationId}")
    public List<Disbursement> getDisbursementsByApplication(@PathVariable Long applicationId) {
        return disbursementService.getDisbursementsByApplication(applicationId);
    }

    @PutMapping("/Disbursement/{id}/milestone-met")
    public ResponseEntity<Disbursement> markMilestoneMet(@PathVariable Long id) {
        Disbursement disbursement = disbursementService.markMilestoneMet(id);
        return disbursement != null ? ResponseEntity.ok(disbursement) : ResponseEntity.notFound().build();
    }

    @PutMapping("/Disbursement/{id}/disburse")
    public ResponseEntity<Disbursement> markDisbursed(@PathVariable Long id) {
        Disbursement disbursement = disbursementService.markDisbursed(id);
        return disbursement != null ? ResponseEntity.ok(disbursement) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/Disbursement/{id}")
    public ResponseEntity<Void> deleteDisbursement(@PathVariable Long id) {
        boolean deleted = disbursementService.deleteDisbursement(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/Disbursement/total/{schemeId}")
    public Double getTotalDisbursedForScheme(@PathVariable Long schemeId) {
        return disbursementService.getTotalDisbursedForScheme(schemeId);
    }
    @GetMapping("/Disbursement/overdue")
    public List<Disbursement> getOverdueDisbursements() {
        return disbursementService.getOverdueDisbursements();
    }
    @GetMapping("/Disbursement/regional-summary")
    public List<Object[]> getTotalDisbursedByRegion() {
        return disbursementService.getTotalDisbursedByRegion();
    }
    @GetMapping("/Disbursement/budget-exhaustion")
    public List<Map<String, Object>> getBudgetExhaustionInsights() {
        return disbursementService.getBudgetExhaustionInsights();
    }
    @GetMapping("/Disbursement/budget-exhaustion/export")
    public ResponseEntity<byte[]> exportBudgetExhaustionExcel() throws IOException {
        byte[] excelBytes = disbursementService.generateBudgetExhaustionExcel();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=budget_exhaustion_report.xlsx")
                .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelBytes);
    }
    @GetMapping("/Disbursement/budget-exhaustion/export-pdf")
    public ResponseEntity<byte[]> exportBudgetExhaustionPdf() throws IOException {
        byte[] pdfBytes = disbursementService.generateBudgetExhaustionPdf();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=budget_exhaustion_report.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfBytes);
    }
}