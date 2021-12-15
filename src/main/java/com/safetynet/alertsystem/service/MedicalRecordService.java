package com.safetynet.alertsystem.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;

@Service
public class MedicalRecordService {

	private static final Logger logger = LogManager.getLogger(MedicalRecordService.class);

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private PersonRepository personRepository;

	// GET all
	public Iterable<MedicalRecord> getAllMedicalRecords() {
		try {
			return medicalRecordRepository.findAll();
		} catch (Exception e) {
			logger.error("Error attempting to get medical records", e);
		}
		return null;
	}

	// POST
	public boolean addMedicalRecord(MedicalRecord medicalRecord) {
		try {
			Optional<MedicalRecord> isMedicalRecordExist = medicalRecordRepository
					.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
			Optional<Person> isPersonExist = personRepository
					.findPersonByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
			if (isMedicalRecordExist.isPresent() || isPersonExist.isEmpty()) {
				return false;
			}
		} catch (Exception e) {
			logger.error("Error attempting to add a new medical record", e);
		}
		medicalRecordRepository.save(medicalRecord);
		return true;
	}

	// PUT
	public Optional<MedicalRecord> updateAnExistingMedicalRecord(MedicalRecord medicalRecord) {
		try {
			Optional<MedicalRecord> medicalRecordToUpdate = medicalRecordRepository
					.findMedicalRecordByFirstNameAndLastName(medicalRecord.getFirstName(), medicalRecord.getLastName());
			if (medicalRecordToUpdate.isPresent()) {
				MedicalRecord medicalRecordUpdated = medicalRecordToUpdate.get();
				medicalRecordUpdated.setBirthdate(medicalRecord.getBirthdate());
				medicalRecordUpdated.setMedications(medicalRecord.getMedications());
				medicalRecordUpdated.setAllergies(medicalRecord.getAllergies());
				medicalRecordRepository.save(medicalRecordUpdated);
				return Optional.of(medicalRecordUpdated);
			}
		} catch (Exception e) {
			logger.error("Error attempting to update an existing medical record", e);
		}
		return Optional.empty();
	}

	// DELETE
	public boolean deleteAMedicalRecord(String firstName, String lastName) {
		try {
			Optional<MedicalRecord> medicalRecordToDelete = medicalRecordRepository
					.findMedicalRecordByFirstNameAndLastName(firstName, lastName);
			if (medicalRecordToDelete.isPresent()) {
				MedicalRecord medicalRecordDeleted = medicalRecordToDelete.get();
				medicalRecordRepository.delete(medicalRecordDeleted);
				return true;
			}
		} catch (Exception e) {
			logger.error("Error attempting to delete a medical record", e);
		}
		return false;
	}

	// =============================
	// Determine age from birth date
	// =============================
	public MedicalRecord getMedicalRecordInfoOfPerson(String firstname, String lastname) {
		MedicalRecord medicalRecordInfo = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(firstname,
				lastname);
		return medicalRecordInfo;
	}

	public int getAgeOf(String firstname, String lastname) {
		MedicalRecord medicalRecord = getMedicalRecordInfoOfPerson(firstname, lastname);
		return countAgeOf(medicalRecord);
	}

	public int countAgeOf(MedicalRecord medicalRecord) {
		LocalDate birthDate = LocalDate.parse(medicalRecord.getBirthdate(), DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		Period agePeriod = Period.between(birthDate, LocalDate.now());

		return agePeriod.getYears();
	}

}
