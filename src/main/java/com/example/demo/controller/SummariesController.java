package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.SummariesEntity;
import com.example.demo.exception.InvalidFormatException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.SummariesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/summaries")
@RequiredArgsConstructor
public class SummariesController {

    private final SummariesService summariesService;

    @PostMapping("/")
    public String generateSummary(@RequestBody String message) throws InvalidFormatException{
        return summariesService.generateTempChatSummary(message);
    }

    @PostMapping("/save")
    public SummariesEntity saveSummary(@RequestBody SummariesEntity summariesEntity) throws InvalidFormatException{
        return summariesService.saveSummary(summariesEntity);
    }

    @GetMapping("/id")
    public SummariesEntity getSummary(@RequestBody String summaryId) throws ItemNotFoundException{
        return summariesService.getSummaryById(summaryId);
    }

    @GetMapping("/getAllPatientSummaries")
    public List<SummariesEntity> findAllPatients(@RequestBody String patientId) throws ItemNotFoundException{
        return summariesService.findAllByPatientId(patientId);
    }

    @GetMapping("/getAllDoctorSummaries")
    public List<SummariesEntity> findAllDoctors(@RequestBody String doctorId) throws ItemNotFoundException{
        return summariesService.findAllByDoctorId(doctorId);
    }
}