package com.example.demo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.MeetingEntity;
import com.example.demo.exception.InvalidFormatException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.MeetingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {
    
    private final MeetingService meetingService;

    @GetMapping("/id")
    public MeetingEntity getMeeting(@RequestBody String meetingId) throws ItemNotFoundException{
        return meetingService.getMeetingByMeetingId(meetingId);
    }

    @PostMapping("/save")
    public MeetingEntity saveMeeting(@RequestBody MeetingEntity meetingEntity) throws InvalidFormatException{
        return meetingService.saveMeeting(meetingEntity);
    }

    @DeleteMapping("/delete")
    public void DeleteMeeting(@RequestBody String meetingId) throws ItemNotFoundException{
        meetingService.deleteByMeetingId(meetingId);
    }
}