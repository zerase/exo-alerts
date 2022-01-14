package com.safetynet.alertsystem.service;

//import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alertsystem.model.DataSourceModel;
import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.FireStationRepository;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;

@Service
public class ReaderJsonService {

	private static final Logger logger = LogManager.getLogger(ReaderJsonService.class);

	@Value("${custom.filename}")
	private String filename;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public void SaveJsonDataInDb() throws Exception {
		try {
			String pathFile = filename;

			ObjectMapper objectMapper = new ObjectMapper();
			DataSourceModel dataSource = objectMapper.readValue(new ClassPathResource(pathFile).getInputStream(), DataSourceModel.class);
			// DataSourceModel dataSource = objectMapper.readValue(new File(pathFile), DataSourceModel.class);

			List<Person> listPersons = dataSource.getPersons();
			personRepository.saveAll(listPersons);

			List<FireStation> listFireStations = dataSource.getFirestations();
			fireStationRepository.saveAll(listFireStations);

			List<MedicalRecord> listMedicalRecords = dataSource.getMedicalrecords();
			medicalRecordRepository.saveAll(listMedicalRecords);

			// System.out.println("Persons : " + listPersons.size() + " entries ==> " + listPersons);
			// System.out.println("FireStations : " + listFireStations.size() + " entries ==> " + listFireStations);
			// System.out.println("MedicalRecords : " + listMedicalRecords.size() + " entries ==> " + listMedicalRecords);

		} catch (Exception e) {
			logger.error("ReaderJsonService Error loading data in SaveJsonDataInDb method", e);
			throw e;
		}
	}

}
