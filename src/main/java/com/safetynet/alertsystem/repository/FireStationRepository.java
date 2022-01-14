package com.safetynet.alertsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsystem.model.FireStation;

@Repository
public interface FireStationRepository extends CrudRepository<FireStation, Long> {

	// for CRUD operation DELETE by station
	Optional<List<FireStation>> findByStation(String stationNumber);

	// for CRUD operations POST, PUT and DELETE by address
	Optional<FireStation> findByAddress(String address);

	// for endpoint 3, endpoint 1 and endpoint 5
	List<FireStation> findFireStationByStation(String stationNumber);

	// for endpoint 4
	List<FireStation> findDistinctByAddress(String Address);

}
