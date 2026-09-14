package com.example.Subsidy_Tracking_System.service;

import com.example.Subsidy_Tracking_System.entity.Scheme;
import com.example.Subsidy_Tracking_System.repository.SchemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchemeService {
    private final SchemeRepository schemeRepository;
    public SchemeService(SchemeRepository schemeRepository)
    {
        this.schemeRepository=schemeRepository;
    }
    public Scheme registerScheme(Scheme scheme)
    {
        return schemeRepository.save(scheme);
    }
    public List<Scheme> getAllScheme()
    {
        return schemeRepository.findAll();
    }
    public Scheme getSchemeById(Long id)
    {
        Optional<Scheme> result=schemeRepository.findById(id);
        if(result.isPresent())
        {
            return result.get();
        }
        else {
            return null;
        }
    }
    public Scheme updateSchemeById(Long id, Scheme updateScheme)
    {
        Optional<Scheme> result=schemeRepository.findById(id);
        if(result.isPresent())
        {
            Scheme existingData=result.get();
            existingData.setSchemeName(updateScheme.getSchemeName());
            existingData.setCategory(updateScheme.getCategory());
            existingData.setRegion(updateScheme.getRegion());
            existingData.setGrantAmount(updateScheme.getGrantAmount());
            existingData.setMinEligibleCriteria(updateScheme.getMinEligibleCriteria());
            existingData.setDescription(updateScheme.getDescription());
            existingData.setActive(updateScheme.isActive());
            existingData.setMaxIncomeLimit(updateScheme.getMaxIncomeLimit());
            existingData.setIncomePoints(updateScheme.getIncomePoints());
            existingData.setMinAge(updateScheme.getMinAge());
            existingData.setMaxAge(updateScheme.getMaxAge());
            existingData.setAgePoints(updateScheme.getAgePoints());
            existingData.setTotalBudget(updateScheme.getTotalBudget());
            return schemeRepository.save(existingData);
        }
        else {
            return null;
        }
    }
    public boolean deleteScheme(Long id)
    {
        if(schemeRepository.existsById(id))
        {
            schemeRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

}
