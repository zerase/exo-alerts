package com.safetynet.alertsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.PersonRepository;

@SpringBootTest
public class PersonServiceTest {
	
	private Person person;
	
	@Autowired
	private PersonService personService;
	
	@MockBean
	private PersonRepository personRepositoryMock;
	
	
	@BeforeEach
	void setUp() {
		person = new Person();
		person.setId(1);
		person.setFirstName("personServiceTestFirstName");
		person.setLastName("personServiceTestLastName");
		person.setAddress("personServiceTestAddress");
		person.setCity("personServiceTestCity");
		person.setZip("12345");
		person.setPhone("111-222-3333");
		person.setEmail("personServiceTestEmail@email.com");
	}
	
	@Test
	void testGetAllPersons_shouldReturnIsNotNull() {
		List<Person> personsList = new ArrayList<Person>();
		personsList.add(person);
		
		when(personRepositoryMock.findAll()).thenReturn(personsList);
		
		assertThat(personService.getAllPersons()).isNotNull();
		assertThat(personService.getAllPersons()).isEqualTo(personsList);
	}
	
	@Test
	void testAddPerson_withSuccess() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
		
		assertThat(personService.addPerson(person)).isNotNull();
		assertThat(personService.addPerson(person)).isEqualTo(Optional.of(person));	
	}
	
	@Test
	void testAddPerson_withErrorBecausePersonAlreadyExist() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
		
		assertThat(personService.addPerson(person)).isNotNull();
		assertThat(personService.addPerson(person)).isEqualTo(Optional.empty());	
	}
	
	@Test
	void testUpdatePerson_withSuccess() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
		
		assertThat(personService.updatePerson(person)).isEqualTo(Optional.of(person));
	}
	
	@Test
	void testUpdatePerson_withErrorBecausePersonNotExist() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
		
		assertThat(personService.updatePerson(person)).isEqualTo(Optional.empty());
	}
	
	@Test
	void testDeletePerson_withSuccess() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(person));
		
		assertThat(personService.deletePerson(person.getFirstName(), person.getLastName())).isTrue();
	}
	
	@Test
	void testDeletePerson_withErrorBecausePersonNotExist() {
		when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
		
		assertThat(personService.deletePerson(person.getFirstName(), person.getLastName())).isFalse();
	}

}
