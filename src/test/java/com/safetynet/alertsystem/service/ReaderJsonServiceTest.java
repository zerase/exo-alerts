package com.safetynet.alertsystem.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.FireStationRepository;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;

@ContextConfiguration
@SpringBootTest
public class ReaderJsonServiceTest {

	@Autowired
	ReaderJsonService readerJsonService;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Test
	void testSaveJsonDataInDb() throws Exception {
		Iterable<Person> personsInDb = personRepository.findAll();
		Iterable<FireStation> fireStationsInDb = fireStationRepository.findAll();
		Iterable<MedicalRecord> medicalRecordsInDb = medicalRecordRepository.findAll();

		readerJsonService.SaveJsonDataInDb();

		assertThat(personsInDb).asList().size().isEqualTo(23);
		assertThat(fireStationsInDb).asList().size().isEqualTo(13);
		assertThat(medicalRecordsInDb).asList().size().isEqualTo(23);
	}

}
