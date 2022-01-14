package com.safetynet.alertsystem.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsystem.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {

	// for CRUD operations POST, PUT and DELETE
	Optional<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);

	// for endpoint 6, endpoint 1, endpoint 2, endpoint 4 and endpoint 5
	MedicalRecord findMedicalRecordInfoByFirstNameAndLastName(String firstname, String lastname);

}
