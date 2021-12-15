package com.safetynet.alertsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.service.MedicalRecordService;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private MedicalRecordService medicalRecordService;
	
	
	@Test
    void testGetAllMedicalRecordsInDb_shouldReturnIsOk() throws Exception {
        mockMvc.perform(get("/medicalRecords")).andExpect(status().isOk());
    }

    @Test
    void testAddMedicalRecord_shouldReturnIsOk() throws Exception {
        String bodyContent = "{ "
        		+ "\"firstName\":\"firstNameTest\", "
        		+ "\"lastName\":\"lastNameTest\", "
        		+ "\"birthdate\":\"01/01/1970\", "
        		+ "\"medications\":[\"medicationTest1:111mg\", "
        		+ "\"medicationTest2:222mg\"], "
        		+ "\"allergies\":[\"allergiesTest1\"] "
        		+ "}";
        
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);
        
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddMedicalRecord_shouldReturnIsConflict() throws Exception {
        String bodyContent = "{ "
        		+ "\"firstName\":\"firstNameTest\", "
        		+ "\"lastName\":\"lastnameTest\", "
        		+ "\"birthdate\":\"01/01/1970\", "
        		+ "\"medications\":[\"medicationTest1:111mg\", \"medicationTest2:222mg\"], "
        		+ "\"allergies\":[\"allergiesTest1\"] "
        		+ "}";
        
        when(medicalRecordService.addMedicalRecord(any(MedicalRecord.class))).thenReturn(false);
        
        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateAnExistingMedicalRecord_shouldReturnIsOk() throws Exception {
        MedicalRecord medicalRecordTest = new MedicalRecord();
        medicalRecordTest.setFirstName("firstNameTest");
        medicalRecordTest.setLastName("lastNameTest");
        medicalRecordTest.setBirthdate("01/01/1970");
        medicalRecordTest.setMedications(List.of("medicationsTest1:123mg", "medicationsTest2:456mg"));
        medicalRecordTest.setAllergies(List.of("allergiesTest1"));
        
        String bodyContent = "{ "
        		+ "\"firstName\":\"firstnameTest\", "
        		+ "\"lastName\":\"lastnameTest\", "
        		+ "\"birthdate\":\"01/01/1970\", "
        		+ "\"medications\":[\"medicationTest1:123mg\", \"medicationTest2:456mg\"], "
        		+ "\"allergies\":[\"allergiesTest1\"] "
        		+ "}";
        
        when(medicalRecordService.updateAnExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(Optional.of(medicalRecordTest));
        
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAnExistingMedicalRecord_shouldReturNotFound() throws Exception {
        String bodyContent = "{ "
        		+ "\"firstName\":\"firstNameTest\", "
        		+ "\"lastName\":\"lastNameTest\", "
        		+ "\"birthdate\":\"01/01/1970\", "
        		+ "\"medications\":[\"medicationTest1:123mg\", \"medicationTest2:456mg\"], "
        		+ "\"allergies\":[\"allergiesTest1\"] "
        		+ "}";
        
        when(medicalRecordService.updateAnExistingMedicalRecord(any(MedicalRecord.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAMedicalRecord_shouldReturnIsOk() throws Exception {
        String paramFirstName = "firstNameTest";
        String paramLastName = "lastNameTest";
        
        when(medicalRecordService.deleteAMedicalRecord(any(String.class), any(String.class))).thenReturn(true);
        
        mockMvc.perform(delete("/medicalRecord?firstName=" + paramFirstName + "&lastName=" + paramLastName))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAMedicalRecordAndResponseNotFound() throws Exception {
    	String paramFirstName = "firstNameTest";
        String paramLastName = "lastNameTest";
    	
        when(medicalRecordService.deleteAMedicalRecord(any(String.class), any(String.class))).thenReturn(false);
        
        mockMvc.perform(delete("/medicalRecord?firstName=" + paramFirstName + "&lastName=" + paramLastName))
                .andExpect(status().isNotFound());
    }

}
