package com.example.Subsidy_Tracking_System.controller;

import com.example.Subsidy_Tracking_System.entity.Scheme;
import com.example.Subsidy_Tracking_System.service.SchemeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SchemeController {
    private final SchemeService schemeService;
    public SchemeController(SchemeService schemeService)
    {
        this.schemeService=schemeService;
    }
    @PostMapping("/Scheme")
    public Scheme addScheme(@RequestBody Scheme scheme)
    {
        return schemeService.registerScheme(scheme);
    }
    @GetMapping("/Scheme")
    public List<Scheme> getAllScheme()
    {
        return schemeService.getAllScheme();
    }
    @GetMapping("/Scheme/{id}")
    public ResponseEntity<Scheme> getSchemeById(@PathVariable Long id)
    {
        Scheme scheme=schemeService.getSchemeById(id);
        if(scheme!=null)
        {
            return ResponseEntity.ok(scheme);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/Scheme/{id}")
    public ResponseEntity<Scheme> upadateScheme(@PathVariable Long id, @RequestBody Scheme updatedScheme)
    {
        Scheme scheme=schemeService.updateSchemeById(id,updatedScheme);
        if(scheme!=null)
        {
            return ResponseEntity.ok(scheme);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/Scheme/{id}")
    public ResponseEntity<Void> deleteScheme(@PathVariable Long id)
    {
        boolean scheme= schemeService.deleteScheme(id);
        if(scheme)
        {
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
