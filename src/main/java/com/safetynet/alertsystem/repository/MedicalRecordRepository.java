package com.safetynet.alertsystem.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsystem.model.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Long> {

	Optional<MedicalRecord> findMedicalRecordByFirstNameAndLastName(String firstName, String lastName);

	// for endpoint 6 and endpoint 1
	MedicalRecord findMedicalRecordInfoByFirstNameAndLastName(String firstname, String lastname);

}
