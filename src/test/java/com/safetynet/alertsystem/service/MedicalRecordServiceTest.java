package com.safetynet.alertsystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;

@SpringBootTest
public class MedicalRecordServiceTest {
	
	private MedicalRecord medicalRecord;
	private Person person;
	
	@Autowired
	private MedicalRecordService medicalRecordService;
	
	@MockBean
	private MedicalRecordRepository medicalRecordRepositoryMock;
	@MockBean
	private PersonRepository personRepositoryMock;

	
	@BeforeEach
    public void setUp() {
        medicalRecord = new MedicalRecord();
        medicalRecord.setId(1);
        medicalRecord.setFirstName("firstNameTest");
        medicalRecord.setLastName("lastNameTest");
        medicalRecord.setBirthdate("03/04/1974");
        medicalRecord.setMedications(new ArrayList<>(Collections.singleton("aspirin : 12g")));
        medicalRecord.setAllergies(new ArrayList<>(Collections.singleton("peanut")));
        person = new Person();
        person.setId(1);
        person.setFirstName("fstNameTest");
        person.setLastName("lstNameTest");
        person.setAddress("addressTest");
        person.setCity("cityTest");
        person.setZip("12345");
        person.setPhone("123-456-7890");
        person.setEmail("personTest@email.com");
    }

    @Test
    void testGetAllMedicalRecords_shouldReturnIsNotNull(){
        List<MedicalRecord> medicalRecordArrayList = new ArrayList<MedicalRecord>();
        medicalRecordArrayList.add(medicalRecord);
        
        when(medicalRecordRepositoryMock.findAll()).thenReturn(medicalRecordArrayList);
        
        assertThat(medicalRecordService.getAllMedicalRecords()).isNotNull();
        assertThat(medicalRecordService.getAllMedicalRecords()).isEqualTo(medicalRecordArrayList);
    }

    @Test
    void testAddMedicalRecord_shouldReturnFalseIfPersonNotExist() {
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.empty());
        when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.empty());
        
        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isFalse();
    }

    @Test
    void testAddMedicalRecord_shouldReturnFalseIfPersonExistButAlreadyHaveAMedicalRecord() {
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.of(person));
        
        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isFalse();
    }

    @Test
    void testAddMedicalRecord_shouldReturnTrueIfPersonExistAndDontHaveAMedicalRecordYet() {
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(personRepositoryMock.findPersonByFirstNameAndLastName(any(String.class),any(String.class))).thenReturn(Optional.of(person));
        
        assertThat(medicalRecordService.addMedicalRecord(medicalRecord)).isTrue();
    }

    @Test
    void testUpdateAnExistingMedicalRecord_withSuccess() {
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        
        assertThat(medicalRecordService.updateAnExistingMedicalRecord(medicalRecord)).isNotNull();
        assertThat(medicalRecordService.updateAnExistingMedicalRecord(medicalRecord)).isEqualTo(Optional.of(medicalRecord));
    }

    @Test
    void testUpdateAMedicalRecord_WithErrorBecauseNotExist() {
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        
        assertThat(medicalRecordService.updateAnExistingMedicalRecord(medicalRecord)).isNotNull();
        assertThat(medicalRecordService.updateAnExistingMedicalRecord(medicalRecord)).isEmpty();
    }

    @Test
    void testDeleteAMedicalRecord_withSuccess() {
        String firstName = "firstNameTest";
        String lastName = "lastNameTest";
        
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.of(medicalRecord));
        
        assertThat(medicalRecordService.deleteAMedicalRecord(firstName, lastName)).isTrue();
    }

    @Test
    void testDeleteAMedicalRecord_withErrorBecauseNotExist() {
    	String firstName = "firstNameTest";
        String lastName = "lastNameTest";
        
        when(medicalRecordRepositoryMock.findMedicalRecordByFirstNameAndLastName(any(String.class), any(String.class))).thenReturn(Optional.empty());
        
        assertThat(medicalRecordService.deleteAMedicalRecord(firstName, lastName)).isFalse();
    }
}
