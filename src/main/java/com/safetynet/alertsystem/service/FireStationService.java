package com.safetynet.alertsystem.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.repository.FireStationRepository;

@Service
public class FireStationService {

	private static final Logger logger = LogManager.getLogger(FireStationService.class);

	@Autowired
	private FireStationRepository fireStationRepository;


	// GET all
	public Iterable<FireStation> getAllFireStations() {
		try {
			return fireStationRepository.findAll();
		} catch (Exception e) {
			logger.error("Error attempting to get fire stations", e);
		}
		return null;
	}





	// POST
	public Optional<FireStation> addFireStationAndAddressMapping(FireStation fireStation) {
		try {
			Optional<FireStation> isFireStationAddressAlreadyExist = fireStationRepository.findByAddress(fireStation.getAddress());
			if (isFireStationAddressAlreadyExist.isEmpty()) {
				fireStationRepository.save(fireStation);
				return Optional.of(fireStation);
			}
		} catch (Exception e) {
			logger.error("Error attempting to add a new firestation", e);
		}
		return Optional.empty();
	}





	// PUT
	public Optional<FireStation> updateFireStationNumberOfAnAddress(FireStation fireStation) {
		try {
			Optional<FireStation> fireStationToUpdate = fireStationRepository.findByAddress(fireStation.getAddress());

			if (fireStationToUpdate.isPresent()) {
				FireStation fireStationUpdated = fireStationToUpdate.get();

				fireStationUpdated.setStation(fireStation.getStation());

				fireStationRepository.save(fireStationUpdated);
				return Optional.of(fireStationUpdated);
			}
		} catch (Exception e) {
			logger.error("Error attempting to update a fire station", e);
		}
		return Optional.empty();
	}





	// DELETE by station
	public Optional<List<FireStation>> deleteMappingOfAStation(String stationNumber) {
		try {
			Optional<List<FireStation>> fireStationsToDelete = fireStationRepository.findByStation(stationNumber);
			if (fireStationsToDelete.isPresent()) {
				List<FireStation> listFireStationsToDelete = fireStationsToDelete.get();
				
				for (FireStation currentFireStation : listFireStationsToDelete) {
					fireStationRepository.delete(currentFireStation);
				}
				return fireStationsToDelete;
			}
		} catch (Exception e) {
			logger.error("Error attempting to delete a fire station by station number", e);
		}
		return Optional.empty();
	}





	// DELETE by address
	public Optional<FireStation> deleteMappingOfAnAddress(String address) {
		try {
			Optional<FireStation> fireStationToDelete = fireStationRepository.findByAddress(address);
			if (fireStationToDelete.isPresent()) {
				FireStation fsToDelete = fireStationToDelete.get();
				fireStationRepository.delete(fsToDelete);
			}
			return fireStationToDelete;
		} catch (Exception e) {
			logger.error("Error attempting to delete a fire station by address", e);
		}
		return Optional.empty();
	}

}
