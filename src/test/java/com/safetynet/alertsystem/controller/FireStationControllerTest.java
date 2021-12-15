package com.safetynet.alertsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.service.FireStationService;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private FireStationService fireStationService;
	
	
	@Test
	void testGetAllFireStations_shouldReturnIsOk() throws Exception {
		mockMvc.perform(get("/firestations")).andExpect(status().isOk());
	}
	
	@Test
    void testAddStationAndAddressMapping_shouldReturnIsCreated() throws Exception {
        FireStation fireStationTest = new FireStation();
        String bodyContent = "{\"address\": \"addressTest\", \"station\": \"77\"}";
        
        when(fireStationService.addFireStationAndAddressMapping(any(FireStation.class))).thenReturn(Optional.of(fireStationTest));
        
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
    }
	
	@Test
    void testAddStationAndAddressMapping_shouldReturnNotFoundBecauseAlreadyExist() throws Exception {
        String bodyContent = "{\"address\": \"addressTest\", \"station\": \"77\"}";
        
        when(fireStationService.addFireStationAndAddressMapping(any(FireStation.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateFireStationNumberOfAnAddress_shouldReturnIsOk() throws Exception {
        FireStation fireStationTest = new FireStation();
        String bodyContent = "{\"address\": \"addressTest\", \"station\": \"77\"}";
        
        when(fireStationService.updateFireStationNumberOfAnAddress(any(FireStation.class))).thenReturn(Optional.of(fireStationTest));
        
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateFireStationNumberOfAnAddress_shouldReturnNotFound() throws Exception {
        String bodyContent = "{\"address\": \"addressTest\", \"station\": \"77\"}";
        when(fireStationService.updateFireStationNumberOfAnAddress(any(FireStation.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMappingOfAStation_shouldReturnIsOk() throws Exception {
        ArrayList<FireStation> fireStationListTest = new ArrayList<FireStation>();
        
        when(fireStationService.deleteMappingOfAStation(any(String.class))).thenReturn(Optional.of(fireStationListTest));
        
        mockMvc.perform(delete("/firestation/station/77")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMappingOfAStation_shouldReturnNotFound() throws Exception {
        when(fireStationService.deleteMappingOfAStation(any(String.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(delete("/firestation/station/77")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteMappingOfAnAddress_shouldReturnIsOk() throws Exception {
        FireStation fireStationTest = new FireStation();
        
        when(fireStationService.deleteMappingOfAnAddress(any(String.class))).thenReturn(Optional.of(fireStationTest));
        
        mockMvc.perform(delete("/firestation/address/addressTest"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMappingOfAnAddress_shouldReturnNotFound() throws Exception {
        when(fireStationService.deleteMappingOfAnAddress(any(String.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(delete("/firestation/address/addressTest")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
