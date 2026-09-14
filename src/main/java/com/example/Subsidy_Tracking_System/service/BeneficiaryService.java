package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.Beneficiary;
import com.example.Subsidy_Tracking_System.repository.BeneficiaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryService(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public Beneficiary registerBeneficiary(Beneficiary beneficiary) {
        beneficiary.setIdentityVerified(false);
        return beneficiaryRepository.save(beneficiary);
    }
    public List<Beneficiary> getAllBeneficiaries()
    {
        return beneficiaryRepository.findAll();
    }
    public  Beneficiary getBeneficiaryById(Long id)
    {
        Optional<Beneficiary> result=beneficiaryRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }
        else
        {
            return null;
        }
    }
    public Beneficiary updateBeneficiaryById(Long id, Beneficiary updatedBeneficiary)
    {
        Optional<Beneficiary> result=beneficiaryRepository.findById(id);
        if(result.isPresent())
        {
            Beneficiary existedData=result.get();
            existedData.setAddress(updatedBeneficiary.getAddress());
            existedData.setCategory(updatedBeneficiary.getCategory());
            existedData.setContactNumber(updatedBeneficiary.getContactNumber());
            existedData.setFullName(updatedBeneficiary.getFullName());
            existedData.setIdentityNumber(updatedBeneficiary.getIdentityNumber());
            existedData.setAge(updatedBeneficiary.getAge());
            existedData.setGender(updatedBeneficiary.getGender());
            existedData.setIncome(updatedBeneficiary.getIncome());
            existedData.setOccupation(updatedBeneficiary.getOccupation());
            existedData.setRegion(updatedBeneficiary.getRegion());
            return beneficiaryRepository.save(existedData);
        }
        else {
            return null;
        }
    }
    public boolean deleteBeneficiary(Long id)
    {
        if(beneficiaryRepository.existsById(id))
        {
            beneficiaryRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }
    public Beneficiary uploadIdentityDocument(Long id, MultipartFile file) throws IOException {
        Optional<Beneficiary> result = beneficiaryRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        }

        Beneficiary beneficiary = result.get();

        String uploadDir = "C:/subsidy-uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "beneficiary_" + id + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        file.transferTo(new File(filePath));

        beneficiary.setIdentityDocumentPath(filePath);
        return beneficiaryRepository.save(beneficiary);
    }
    public Beneficiary verifyIdentity(Long id) {
        Optional<Beneficiary> result = beneficiaryRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        }

        Beneficiary beneficiary = result.get();

        if (beneficiary.getIdentityDocumentPath() == null) {
            return null;
        }

        beneficiary.setIdentityVerified(true);
        return beneficiaryRepository.save(beneficiary);
    }
    public List<Object[]> getBeneficiaryCountByCategory() {
        return beneficiaryRepository.getBeneficiaryCountByCategory();
    }
}