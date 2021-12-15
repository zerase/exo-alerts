package com.safetynet.alertsystem.controller;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.service.FireStationService;

@RestController
public class FireStationController {

	private static final Logger logger = LogManager.getLogger(FireStationController.class);

	@Autowired
	private FireStationService fireStationService;

	// ===================
	//   CRUD Operations
	// ===================

	// CREATE - Add a new fire station
	@PostMapping("/firestation")
	public ResponseEntity<?> addStationAndAddressMapping(@RequestBody FireStation firestation) {
		logger.info("POST Request '/firestation' received");

		if (firestation == null || firestation.getAddress().isBlank() || firestation.getStation().isBlank()) {
			logger.error("FireStationController error : Address and Station must be set");
			return new ResponseEntity<>("Address and Station must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<FireStation> createdFireStation = fireStationService.addFireStationAndAddressMapping(firestation);

		if (createdFireStation.isPresent()) {
			logger.info("FireStation successfully added");
			return new ResponseEntity<>(createdFireStation, HttpStatus.CREATED);
		} else {
			logger.info("FireStation mapping already exist");
			return new ResponseEntity<>("FireStation mapping already exist", HttpStatus.NOT_FOUND);
		}

	}

	// READ - Get all fire stations
	@GetMapping("/firestations")
	public Iterable<FireStation> getAllFireStations() {
		logger.info("GET Request '/firestation' received");

		Iterable<FireStation> fireStationsInDb = fireStationService.getAllFireStations();

		return fireStationsInDb;
	}

	// UPDATE - Update an existing fire station
	@PutMapping("/firestation")
	public ResponseEntity<?> updateFireStationNumberOfAnAddress(@RequestBody FireStation firestation) {
		logger.info("PUT request '/firestation' received");

		if (firestation == null || firestation.getAddress().isBlank() || firestation.getStation().isBlank()) {
			logger.error("FireStationController error : Address and Station must be set");
			return new ResponseEntity<>("Address and Station must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<FireStation> updatedFireStation = fireStationService.updateFireStationNumberOfAnAddress(firestation);

		if (updatedFireStation.isPresent()) {
			logger.info("Fire station successfully modified");
			return new ResponseEntity<>(updatedFireStation, HttpStatus.OK);
		} else {
			logger.info("Fire station not found");
			return new ResponseEntity<>("Fire station not found", HttpStatus.NOT_FOUND);
		}
	}

	// DELETE - Delete a fire station by station number
	@DeleteMapping("/firestation/station/{stationNumber}")
	public ResponseEntity<?> deleteMappingOfAStation(@PathVariable("stationNumber") final String stationNumber) {
		logger.info("DELETE request '/firestation' received");

		if (stationNumber.isBlank()) {
			logger.error("FireStationController error : Station number must be set");
			return new ResponseEntity<>("Station number must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<List<FireStation>> deletedFireStations = fireStationService.deleteMappingOfAStation(stationNumber);

		if (deletedFireStations.isPresent()) {
			logger.info("Station " + stationNumber + " successfully deleted");
			return new ResponseEntity<>("Deleted fire station " + stationNumber + " successfully", HttpStatus.OK);
		} else {
			logger.info("Station " + stationNumber + " not found : " + deletedFireStations);
			return new ResponseEntity<>("Station not found", HttpStatus.NOT_FOUND);
		}
	}

	// DELETE - Delete a fire station by address
	@DeleteMapping("/firestation/address/{address}")
	public ResponseEntity<?> deleteMappingOfAnAddress(@PathVariable("address") final String address) {
		logger.info("DELETE request '/firestation' received");

		if (address.isBlank()) {
			logger.error("FireStationController error : Address must be set");
			return new ResponseEntity<>("Address must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<FireStation> deletedFireStation = fireStationService.deleteMappingOfAnAddress(address);

		if (deletedFireStation.isPresent()) {
			logger.info("Address " + address + " successfully deleted : " + deletedFireStation);
			return new ResponseEntity<>("Deleted address " + address + " successfully", HttpStatus.OK);
		} else {
			logger.info("Address " + address + " not found");
			return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
		}

	}

}
