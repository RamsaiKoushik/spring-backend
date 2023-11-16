package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.MedicineEntity;
import com.example.demo.exception.InvalidFormatException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.MedicineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medicine")
@RequiredArgsConstructor
public class MedicineController {
    
    private final MedicineService medicineService;

    @GetMapping("/getMedicines")
    public List<MedicineEntity> getMedicines(String medicineId) throws ItemNotFoundException{
        return medicineService.getMedicinesList(medicineId);
    }

    @PostMapping("/save")
    public MedicineEntity saveMedicine(MedicineEntity medicineEntity) throws InvalidFormatException{
        return medicineService.saveMedicine(medicineEntity);
    }
}