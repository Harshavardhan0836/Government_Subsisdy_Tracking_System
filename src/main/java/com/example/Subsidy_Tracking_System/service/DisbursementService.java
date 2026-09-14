package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.*;
import com.example.Subsidy_Tracking_System.repository.ApplicationRepository;
import com.example.Subsidy_Tracking_System.repository.DisbursementRepository;
import com.example.Subsidy_Tracking_System.repository.SchemeRepository;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.util.*;

@Service
public class DisbursementService {

    private final DisbursementRepository disbursementRepository;
    private final ApplicationRepository applicationRepository;
    private final SchemeRepository schemeRepository;
    private final AuditLogService auditLogService;

    public DisbursementService(DisbursementRepository disbursementRepository,
                               ApplicationRepository applicationRepository, SchemeRepository schemeRepository, AuditLogService auditLogService) {
        this.disbursementRepository = disbursementRepository;
        this.applicationRepository = applicationRepository;
        this.schemeRepository = schemeRepository;
        this.auditLogService=auditLogService;
    }

    public Disbursement createDisbursement(Long applicationId, Disbursement disbursement) {
        Optional<Application> appResult = applicationRepository.findById(applicationId);
        if (appResult.isEmpty()) {
            return null;
        }

        disbursement.setApplication(appResult.get());
        disbursement.setDueDate(java.time.LocalDate.now().plusDays(30));
        disbursement.setStatus(DisbursementStatus.PENDING);
        return disbursementRepository.save(disbursement);
    }

    public List<Disbursement> getDisbursementsByApplication(Long applicationId) {
        return disbursementRepository.findByApplicationId(applicationId);
    }

    public Disbursement markMilestoneMet(Long disbursementId) {
        Optional<Disbursement> result = disbursementRepository.findById(disbursementId);
        if (result.isEmpty()) {
            return null;
        }

        Disbursement disbursement = result.get();
        disbursement.setStatus(DisbursementStatus.MILESTONE_MET);
        return disbursementRepository.save(disbursement);
    }

    public Disbursement markDisbursed(Long disbursementId) {
        Optional<Disbursement> result = disbursementRepository.findById(disbursementId);
        if (result.isEmpty()) {
            return null;
        }

        Disbursement disbursement = result.get();

        if (disbursement.getStatus() != DisbursementStatus.MILESTONE_MET) {
            return null;
        }

        disbursement.setStatus(DisbursementStatus.DISBURSED);
        disbursement.setDisbursedDate(java.time.LocalDate.now());
        auditLogService.log("DISBURSEMENT_RELEASED",
                "Disbursement ID " + disbursement.getId() + ", amount " + disbursement.getAmount() + " released.");
        return disbursementRepository.save(disbursement);
    }
    public boolean deleteDisbursement(Long id) {
        if (disbursementRepository.existsById(id)) {
            disbursementRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    public Double getTotalDisbursedForScheme(Long schemeId) {
        Double total = disbursementRepository.getTotalDisbursedForScheme(schemeId);
        return total != null ? total : 0.0;
    }
    public List<Disbursement> getOverdueDisbursements() {
        return disbursementRepository.findOverdueDisbursements();
    }
    public List<Object[]> getTotalDisbursedByRegion() {
        return disbursementRepository.getTotalDisbursedByRegion();
    }
    public List<Map<String, Object>> getBudgetExhaustionInsights() {
        List<Object[]> disbursedTotals = disbursementRepository.getTotalDisbursedGroupedByScheme();
        List<Scheme> allSchemes = schemeRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Scheme scheme : allSchemes) {
            Double disbursed = 0.0;

            for (Object[] row : disbursedTotals) {
                Long schemeId = (Long) row[0];
                if (schemeId.equals(scheme.getId())) {
                    disbursed = (Double) row[1];
                    break;
                }
            }

            Double budget = scheme.getTotalBudget() != null ? scheme.getTotalBudget() : 0.0;
            Double percentUsed = budget > 0 ? (disbursed / budget) * 100 : 0.0;

            Map<String, Object> entry = new HashMap<>();
            entry.put("schemeName", scheme.getSchemeName());
            entry.put("totalBudget", budget);
            entry.put("totalDisbursed", disbursed);
            entry.put("percentUsed", percentUsed);
            result.add(entry);
        }

        return result;
    }
    public byte[] generateBudgetExhaustionExcel() throws IOException {
        List<Map<String, Object>> data = getBudgetExhaustionInsights();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Budget Exhaustion");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Scheme Name");
        headerRow.createCell(1).setCellValue("Total Budget");
        headerRow.createCell(2).setCellValue("Total Disbursed");
        headerRow.createCell(3).setCellValue("Percent Used");

        int rowNum = 1;
        for (Map<String, Object> entry : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) entry.get("schemeName"));
            row.createCell(1).setCellValue((Double) entry.get("totalBudget"));
            row.createCell(2).setCellValue((Double) entry.get("totalDisbursed"));
            row.createCell(3).setCellValue((Double) entry.get("percentUsed"));
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
    public byte[] generateBudgetExhaustionPdf() throws IOException {
        List<Map<String, Object>> data = getBudgetExhaustionInsights();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float startY = 750;
        float rowHeight = 20;
        float margin = 50;

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        contentStream.newLineAtOffset(margin, startY);
        contentStream.showText("Budget Exhaustion Report");
        contentStream.endText();

        startY -= 40;

        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        contentStream.newLineAtOffset(margin, startY);
        contentStream.showText("Scheme Name          Total Budget      Disbursed        % Used");
        contentStream.endText();

        startY -= rowHeight;

        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

        for (Map<String, Object> entry : data) {
            contentStream.beginText();
            contentStream.newLineAtOffset(margin, startY);
            String line = entry.get("schemeName") + "   " + entry.get("totalBudget") + "   "
                    + entry.get("totalDisbursed") + "   " + entry.get("percentUsed") + "%";
            contentStream.showText(line);
            contentStream.endText();
            startY -= rowHeight;
        }

        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}