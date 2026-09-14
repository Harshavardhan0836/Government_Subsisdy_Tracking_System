package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.Beneficiary;
import com.example.Subsidy_Tracking_System.service.BeneficiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.List;

@RestController
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;
    public BeneficiaryController(BeneficiaryService beneficiaryService)
    {
        this.beneficiaryService=beneficiaryService;
    }
 @PostMapping("/Beneficiary")
    public Beneficiary addBenificiary(@RequestBody Beneficiary beneficiary)
 {
     return beneficiaryService.registerBeneficiary(beneficiary);
 }
 @GetMapping("/Beneficiary")
    public List<Beneficiary> getAllBeneficiaries()
 {
     return beneficiaryService.getAllBeneficiaries();
 }
 @GetMapping("/Beneficiary/{id}")
public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id)
 {
     Beneficiary beneficiary=beneficiaryService.getBeneficiaryById(id);
     if(beneficiary!=null)
     {
         return ResponseEntity.ok(beneficiary);
     }
     else {
         return ResponseEntity.notFound().build();
     }
 }
 @PutMapping("/Beneficiary/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@PathVariable Long id, @RequestBody Beneficiary updateBeneficiary)
 {
     Beneficiary beneficiary=beneficiaryService.updateBeneficiaryById(id, updateBeneficiary);
     if(beneficiary!=null)
     {
         return ResponseEntity.ok(beneficiary);
     }
     else {
         return ResponseEntity.notFound().build();
     }
 }
 @DeleteMapping("/Beneficiary/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id)
 {
     boolean beneficiary=beneficiaryService.deleteBeneficiary(id);
     if(beneficiary)
     {
         return ResponseEntity.noContent().build();
     }
     else {
         return ResponseEntity.notFound().build();
     }
 }
    @PostMapping("/Beneficiary/{id}/upload-document")
    public ResponseEntity<Beneficiary> uploadDocument(@PathVariable Long id,
                                                      @RequestParam("file") MultipartFile file) throws IOException {
        Beneficiary beneficiary = beneficiaryService.uploadIdentityDocument(id, file);
        return beneficiary != null ? ResponseEntity.ok(beneficiary) : ResponseEntity.notFound().build();
    }
    @PutMapping("/Beneficiary/{id}/verify-identity")
    public ResponseEntity<Beneficiary> verifyIdentity(@PathVariable Long id) {
        Beneficiary beneficiary = beneficiaryService.verifyIdentity(id);
        return beneficiary != null ? ResponseEntity.ok(beneficiary) : ResponseEntity.notFound().build();
    }
    @GetMapping("/Beneficiary/category-distribution")
    public List<Object[]> getBeneficiaryCountByCategory() {
        return beneficiaryService.getBeneficiaryCountByCategory();
    }
}
