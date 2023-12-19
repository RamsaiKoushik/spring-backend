package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.DoctorEntity;
import com.example.demo.entity.Generate;
import com.example.demo.entity.MessageEntity;
import com.example.demo.entity.PatientEntity;
import com.example.demo.entity.SummariesEntity;
import com.example.demo.entity.Summary;
import com.example.demo.exception.InvalidFormatException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.SummariesRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SummariesServiceImpl implements SummariesService{

    private final SummariesRepository summariesRepository;

    private final MessageRepository messageRepository;

    private final PatientRepository patientRepository;

    @Value("${cohereAi.Key}")
    private String Key;

    public String generateMessage(String message) throws InvalidFormatException {
        String p=message.replace("\r","\n\n");
        final String uri="https://api.cohere.ai/v1/generate";
        final String apiKey="Bearer "+ Key;

        try{
            RestTemplate restTemplate=new RestTemplate();
            HttpHeaders headers=new HttpHeaders();
            headers.set("Authorization",apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Generate requestBody= new Generate();
            requestBody.setTruncate("END");
            requestBody.setReturn_likelihoods("NONE");
            requestBody.setPrompt(p);

            HttpEntity<Generate> requestEntity=new HttpEntity<>(requestBody,headers);

            ResponseEntity<Generate> response=restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                Generate.class
            );

            String finalresponse=response.getBody().getGenerations().get(0).getText();
            return finalresponse;
        }
        catch(Exception ex){
            throw new InvalidFormatException(ex.getMessage());
        }
    }

    
    @Override
    public String generateTempChatSummary(String message, String length, String format) throws InvalidFormatException {
        
        String p=message.replace("\r","\n\n");
        final String uri="https://api.cohere.ai/v1/summarize";
        final String apiKey="Bearer "+ Key;

        //need to add minimum length check;
        try{
            if(message.length()<300){
                return message;
            }
            RestTemplate restTemplate=new RestTemplate();
            HttpHeaders headers=new HttpHeaders();
            headers.set("Authorization",apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            Summary requestBody=new Summary();
            requestBody.setLength(length);
            requestBody.setTemperature(0.2);
            requestBody.setFormat(format);
            requestBody.setExtractiveness("high");
            requestBody.setText(p);

            HttpEntity<Summary> requestEntity=new HttpEntity<>(requestBody,headers);

            ResponseEntity<Summary> response=restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                Summary.class
            );

            String finalresponse=response.getBody().getSummary();
            return finalresponse;
        }
        catch(Exception ex){
            throw new InvalidFormatException(ex.getMessage());
        }
    }

    @Override
    public SummariesEntity saveSummary(SummariesEntity summariesEntity) throws InvalidFormatException {
        try{

            summariesEntity.setSummaryId(UUIDService.getUUID());
            String message="";
            PatientEntity patient=summariesEntity.getPatientEntity();
            PatientEntity doc=summariesEntity.getDoctorEntity().getPatientEntity();

            List<PatientEntity> patients=new ArrayList<>();
            patients.add(doc);
            patients.add(patient);
            List<MessageEntity> messages=messageRepository.findByRecPatientEntityInAndSenPatientEntityInOrderByDateAsc(patients, patients);

            for(int i=0;i<messages.size();i++){
                MessageEntity currentMessage=messages.get(i);
                if(currentMessage.getSenPatientEntity().getPatientId().equals(patient.getPatientId())){
                    PatientEntity p=patientRepository.findById(patient.getPatientId()).get();
                    message += p.getName() + " said " + currentMessage.getText() + "\n";
                }
                else {
                    PatientEntity q=patientRepository.findById(doc.getPatientId()).get();
                    message += q.getName() + " said " + currentMessage.getText() + "\n";
                }
            }

            String finalmessage=generateMessage(message + "Give detailed summary of this conversation?");

            summariesEntity.setText(finalmessage);

            System.out.println(finalmessage);
            return summariesRepository.save(summariesEntity);

        }
        catch(Exception ex){
            throw new InvalidFormatException(ex.getMessage());
        }
    }

    @Override
    public SummariesEntity getSummaryById(String summaryId) throws ItemNotFoundException {
        try{
            Optional<SummariesEntity> patient=summariesRepository.findById(summaryId);
            if(patient.isPresent()) return patient.get();
        }
        catch(Exception ex){
            throw new ItemNotFoundException("This Summary doesn't exist");
        }
        return null;
    }

    @Override
    public List<SummariesEntity> findAllByPatientId(String patientId) throws ItemNotFoundException {
        try{
            PatientEntity patientEntity=new PatientEntity();
            patientEntity.setPatientId(patientId);
            return summariesRepository.findAllByPatientEntity(patientEntity);
        }
        catch(Exception ex){
            throw new ItemNotFoundException("No summaries for this patientId found");
        }
    }

    @Override
    public List<SummariesEntity> findAllByDoctorId(String doctorId) throws ItemNotFoundException {
        try{
            DoctorEntity doctorEntity=new DoctorEntity();
            doctorEntity.setDoctorId(doctorId);
            return summariesRepository.findAllByDoctorEntity(doctorEntity);
        }
        catch(Exception ex){
            throw new ItemNotFoundException("No summaries for this doctorId found");
        }
    }
    
}
