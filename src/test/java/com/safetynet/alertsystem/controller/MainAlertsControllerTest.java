package com.safetynet.alertsystem.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MainAlertsControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	// Endpoint 7
	@Test
	void testGetCommunityEmails_shouldReturnEmailsOfAllPersonLivingInCityCulver() throws Exception {
		mockMvc.perform(get("/communityEmail").param("city", "Culver")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0]", is("jaboyd@email.com")))
				.andExpect(jsonPath("$.size()", is(15)));
	}
	
	@Test
	void testGetCommunityEmails_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/communityEmail").param("city", ""))
				.andExpect(status().isBadRequest());
	}

	// Endpoint 3
	@Test
	void testGetPhonesOfAllPersonCoveredBy_shouldReturnPhonesListOfPersonsCoveredByStation2() throws Exception {
		mockMvc.perform(get("/phoneAlert").param("firestation", "2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0]", is("841-874-6513")))
				.andExpect(jsonPath("$.size()", is(4)));
	}
	
	@Test
	void testGetPhonesOfAllPersonCoveredBy_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/phoneAlert").param("firestation", ""))
				.andExpect(status().isBadRequest());
	}

	// Endpoint 6
	@Test
	void testGetPersonInfos_shouldReturnsPersonInfoListOfPersonJohnBoyd() throws Exception {
		mockMvc.perform(get("/personInfo").param("firstName", "John").param("lastName", "Boyd"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	void testGetPersonInfos_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/personInfo").param("firstName", "").param("lastName", ""))
				.andExpect(status().isBadRequest());
	}
	
	// Endpoint 1
	@Test
	void testgetPersonsListByFireStationCovering_shouldReturnsListOfPersonCoveredByStation3() throws Exception {
		mockMvc.perform(get("/firestation").param("stationNumber", "3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	void testgetPersonsListByFireStationCovering_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/firestation").param("stationNumber", ""))
				.andExpect(status().isBadRequest());
	}
	
	// Endpoint 2
	@Test
	void testGetChildrenList_shouldReturnListOfChildrenLivingAt1509CulverSt() throws Exception {
		mockMvc.perform(get("/childAlert").param("address", "1509 Culver St"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	void testGetChildrenList_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/childAlert").param("address", ""))
				.andExpect(status().isBadRequest());
	}
	
	// Endpoint 4
	@Test
	void testGetPersonsForFireStationAddress_shouldReturnListOfPersonLivingAt1509CulverSt() throws Exception {
		mockMvc.perform(get("/fire").param("address", "1509 Culver St"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	void testGetPersonsForFireStationAddress_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/fire").param("address", ""))
				.andExpect(status().isBadRequest());
	}
	
	// Endpoint 5
	@Test
	void testFloodStations_shouldReturnListOfAllHouseholdsCoveredByStation1And3() throws Exception {
		mockMvc.perform(get("/flood/stations").param("stations", "1,3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty());
	}
	
	@Test
	void testFloodStations_shouldReturnKO() throws Exception {
		mockMvc.perform(get("/flood/stations").param("stations", ""))
				.andExpect(status().isBadRequest());
	}

}
