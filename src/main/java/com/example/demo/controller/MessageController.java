package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.MessageEntity;
import com.example.demo.exception.InvalidFormatException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/id")
    public MessageEntity getMessage(@RequestBody String messageId) throws ItemNotFoundException{
        return messageService.getMessageById(messageId);
    }

    @PostMapping("/")
    public String generateChat(@RequestBody String message) throws InvalidFormatException{
        return messageService.generateMessage(message);
    }

    @GetMapping("/getAllMessages")
    public List<MessageEntity> getAllMessages(@RequestBody MessageEntity messageEntity) throws ItemNotFoundException{
        return messageService.getAllReceiverSender(messageEntity);
    }

    @PostMapping("/saveUsertoBot")
    public MessageEntity saveUserToBot(@RequestBody MessageEntity messageEntity) throws ItemNotFoundException, InvalidFormatException{
        return messageService.saveUserToBotMessage(messageEntity);
    }

    @PostMapping("/saveUsertoUser")
    public MessageEntity saveUserToUser(@RequestBody MessageEntity messageEntity) throws InvalidFormatException{
        return messageService.saveUserToUserMessage(messageEntity);
    }

    @PostMapping("/saveMeetingChat")
    public MessageEntity saveMeetingChat(@RequestBody MessageEntity messageEntity) throws ItemNotFoundException{
        return messageService.saveMeetingChat(messageEntity);
    }

    @DeleteMapping("/delete")
    public void deleteById(@RequestBody String messageId) throws ItemNotFoundException{
        messageService.deleteByMessageId(messageId);
    }

    @DeleteMapping("/deleteAll")
    public void deleteByRecAndSenId(@RequestBody MessageEntity messageEntity) throws ItemNotFoundException{
        messageService.deleteAllByRecIdAndSendId(messageEntity);
    }
}