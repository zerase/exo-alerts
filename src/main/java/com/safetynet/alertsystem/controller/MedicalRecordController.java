package com.safetynet.alertsystem.controller;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);

	@Autowired
	private MedicalRecordService medicalRecordService;

	// ===================
	//   CRUD Operations
	// ===================

	// CREATE - Add a new medical record
	@PostMapping("/medicalRecord")
	public ResponseEntity<?> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {

		logger.info("POST Request '/medicalRecord' received ");

		boolean response = medicalRecordService.addMedicalRecord(medicalRecord);

		if (response) {
			logger.info("Medical record successfully added");
			return new ResponseEntity<>("Medical record successfully created", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Person doesn't exist or medical file already exists ", HttpStatus.CONFLICT);
		}
	}

	// READ - Get all medical records
	@GetMapping("/medicalRecords")
	public Iterable<MedicalRecord> getAllMedicalRecordsInDB() {
		logger.info("GET Request '/medicalRecord' received ");

		Iterable<MedicalRecord> medicalRecordsInDb = medicalRecordService.getAllMedicalRecords();

		return medicalRecordsInDb;
	}

	// UPDATE - Update an existing medical record
	@PutMapping("/medicalRecord")
	public ResponseEntity<?> updateAnExistingMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.info("PUT request '/medicalRecord' received");

		Optional<MedicalRecord> updatedMedicalRecord = medicalRecordService
				.updateAnExistingMedicalRecord(medicalRecord);

		if (updatedMedicalRecord.isPresent()) {
			logger.info("Medical record successfully modified");
			return new ResponseEntity<>("Medical record successfully modified", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Medical record not found", HttpStatus.NOT_FOUND);
		}
	}

	// DELETE - Delete a medical record
	@DeleteMapping("/medicalRecord")
	public ResponseEntity<?> deleteAMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		logger.info("DELETE request '/medicalRecord' received");

		if (firstName.isBlank() || lastName.isBlank()) {
			logger.error("MedicalRecordController error : FirstName and LastName must be set");
			return new ResponseEntity<>("FirstName and LastName must be set", HttpStatus.BAD_REQUEST);
		}

		boolean deletedMedicalRecord = medicalRecordService.deleteAMedicalRecord(firstName, lastName);

		if (deletedMedicalRecord == true) {
			logger.info("Medical record of person " + firstName + " " + lastName + " successfully deleted");
			return new ResponseEntity<>("Deleted medical record of " + firstName + " " + lastName + " successfully",
					HttpStatus.OK);
		} else {
			logger.info("Medical record of person " + firstName + " " + lastName + " not found");
			return new ResponseEntity<>("Medical record not found", HttpStatus.NOT_FOUND);
		}
	}
}
