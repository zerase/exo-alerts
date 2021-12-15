package com.safetynet.alertsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.service.PersonService;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PersonService personService;

	
	@Test
	void testGetAllPersonsInDb_shouldReturnIsOk() throws Exception {
		mockMvc.perform(get("/persons")).andExpect(status().isOk());
	}
	
	@Test
	void testAddNewPerson_shouldReturnIsCreated() throws Exception {
		String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTest\",\n" +
                "\"lastName\":\"lastNameTest\",\n" +
                "\"address\":\"AddressTest\",\n" +
                "\"city\":\"cityTest\",\n" +
                "\"zip\":\"12345\",\n" +
                "\"phone\":\"123-456-7890\",\n" +
                "\"email\":\"emailTest@email.com\"\n" +
                "}";
		
		when(personService.addPerson(any(Person.class))).thenReturn(Optional.of(new Person()));
        
		mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isCreated());
	}
	
	@Test
	void testAddNewPerson_shouldReturnNotFound() throws Exception {
		String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTest\",\n" +
                "\"lastName\":\"lastNameTest\",\n" +
                "\"address\":\"AddressTest\",\n" +
                "\"city\":\"cityTest\",\n" +
                "\"zip\":\"12345\",\n" +
                "\"phone\":\"123-456-7890\",\n" +
                "\"email\":\"emailTest@email.com\"\n" +
                "}";
		
		when(personService.addPerson(any(Person.class))).thenReturn(Optional.empty());
        
		mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
	}
	
	@Test
    void testUpdateExistingPerson_shouldReturnIsOk() throws Exception {
        Person personTest = new Person();
        personTest.setFirstName("firstNameTestUpdateExistingPerson");
        personTest.setLastName("lastNameTestUpdateExistingPerson");
        personTest.setAddress("AddressTestUpdateExistingPerson");
        personTest.setCity("cityTestUpdateExistingPerson");
        personTest.setZip("12345");
        personTest.setPhone("123-456-7890");
        personTest.setEmail("emailTestUpdateAnExistingPerson@email.com");
        
        String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTestUpdateExistingPerson\",\n" +
                "\"lastName\":\"lastNameTestUpdateExistingPerson\",\n" +
                "\"address\":\"AddressTestUpdateExistingPerson\",\n" +
                "\"city\":\"cityTestUpdateExistingPerson\",\n" +
                "\"zip\":\"98765\",\n" +
                "\"phone\":\"987-654-3210\",\n" +
                "\"email\":\"emailTestUpdateExistingPerson@email.com\"\n" +
                "}";
        
        when(personService.updatePerson(any(Person.class))).thenReturn(Optional.of(personTest));
        
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isOk());
	}
	
	@Test
    void testUpdateExistingPerson_shouldReturnNotFound() throws Exception {
        String bodyContent = "{\n" +
                "\"firstName\":\"firstNameTestUpdateExistingPerson\",\n" +
                "\"lastName\":\"lastNameTestUpdateExistingPerson\",\n" +
                "\"address\":\"AddressTestUpdateExistingPerson\",\n" +
                "\"city\":\"cityTestUpdateExistingPerson\",\n" +
                "\"zip\":\"98765\",\n" +
                "\"phone\":\"987-654-3210\",\n" +
                "\"email\":\"emailTestUpdateExistingPerson@email.com\"\n" +
                "}";
        
        when(personService.updatePerson(any(Person.class))).thenReturn(Optional.empty());
        
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyContent))
                .andExpect(status().isNotFound());
    }
	
	@Test
    void testDeletePerson_shouldReturnIsOk() throws Exception {
        String paramFirstName = "firstNameTest";
        String paramLastName = "lastNameTest";
       
        when(personService.deletePerson(any(String.class), any(String.class))).thenReturn(true);
        
        mockMvc.perform(delete("/person?firstName=" + paramFirstName + "&lastName=" + paramLastName))
                .andExpect(status().isOk());
    }
	
	@Test
    void testDeletePerson_shouldReturnNotFound() throws Exception {
        String paramFirstName = "firstNameTest";
        String paramLastName = "lastNameTest";
       
        when(personService.deletePerson(any(String.class), any(String.class))).thenReturn(false);
        
        mockMvc.perform(delete("/person?firstName=" + paramFirstName + "&lastName=" + paramLastName))
                .andExpect(status().isNotFound());
    }
	
}
