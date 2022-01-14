package com.safetynet.alertsystem.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.safetynet.alertsystem.model.PersonDetails;
import com.safetynet.alertsystem.service.MainAlertsService;
import com.safetynet.alertsystem.util.FilterUtil;


@RestController
public class MainAlertsController {

	private static final Logger logger = LogManager.getLogger(MainAlertsController.class);

	@Autowired
	private MainAlertsService mainService;


	// ========== Endpoint url 1 ==========================================
	// GET http://localhost:8080/firestation?stationNumber=<station_number>
	// ====================================================================
	/**
	 * This URL must return a list of people covered by the corresponding fire station.
	 * It also provide a count of the number of adults and the number of children in the service area.
	 * 
	 * @param stationNumber
	 * @return personsCoveredByFireStation
	 */
	@GetMapping("/firestation")
	public ResponseEntity<MappingJacksonValue> getPersonsListByFireStationCovering(@RequestParam String stationNumber) {
		logger.info("GET request /firestation?stationNumber=" + stationNumber);

		if (stationNumber.isBlank()) {
			logger.error("The parameter 'stationNumber' must be set", new Exception("The stationNumber parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Map<Map<String, String>, List<PersonDetails>> personsCoveredByFireStation = mainService.getCoveredPersonsOfFireStation(stationNumber);

		logger.info("Return list of persons covered by firestation number " + stationNumber + " as " + personsCoveredByFireStation);

		return ResponseEntity.ok(FilterUtil.applyPersonDetailsExcludeFilter(personsCoveredByFireStation, "city", "zip", "email", "medications", "allergies"));
	}





	// ========== Endpoint url 2 ============================
	// GET http://localhost:8080/childAlert?address=<address>
	// ======================================================
	/**
	 * This URL must return a list of children (anyone aged 18 or under) living at requested address.
	 * 
	 * @param address
	 * @return childrenList
	 */
	@GetMapping("/childAlert")
	public ResponseEntity<MappingJacksonValue> getChildrenList(@RequestParam String address) {
		logger.info("GET request /childAlert?address=" + address);

		if (address.isBlank()) {
			logger.error("The parameter 'address' must be set", new Exception("The address parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List<List<PersonDetails>> childrenList = mainService.getChildrenLivingAtAddress(address);

		logger.info("Return list of children living at the address " + address + " as " + childrenList);

		return ResponseEntity.ok(FilterUtil.applyPersonDetailsExcludeFilter(childrenList, "address", "city", "zip", "phone", "email", "medications", "allergies"));
	}





	// ========== Endpoint url 3 ===========================================
	// GET http://localhost:8080/phoneAlert?firestation=<firestation_number>
	// =====================================================================
	/**
	 * This URL must return a list of the telephone numbers of the residents served by the fire station. 
	 * We will use it to send emergency text messages to specific households.
	 * 
	 * @param firestation
	 * @return phonesList The list of phone numbers of people served by the number of the fire station passed as a parameter
	 */
	@GetMapping("/phoneAlert")
	public ResponseEntity<List<String>> getPhonesAlert(@RequestParam String firestation) {
		logger.info("GET request /phoneAlert?firestation=" + firestation);

		if (firestation.isBlank()) {
			logger.error("The parameter 'firestation' must be set", new Exception("The firestation parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List<String> phonesList = mainService.getPhonesOfPersonsCoveredByFireStation(firestation);

		if (phonesList.isEmpty()) {
			logger.error("No data found for firestation=" + firestation, new Exception("Nothing found for this firestation parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		logger.info("Return phones list of inhabitants covered by firestation " + firestation + " as " + phonesList);

		return ResponseEntity.ok(phonesList);
	}





	// ========== Endpoint url 4 ======================
	// GET http://localhost:8080/fire?address=<address>
	// ================================================
	/**
	 * This URL must return the list of inhabitants living at the given address as well as the number of the fire station serving it.
	 * 
	 * @param address
	 * @return forFireList
	 */
	@GetMapping("/fire")
	public ResponseEntity<MappingJacksonValue> getPersonsForFireStationAddress(@RequestParam String address) {
		logger.info("GET request /fire?address=" + address);

		if (address.isBlank()) {
			logger.error("The parameter 'address' must be set", new Exception("The address parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Map<Set<String>, List<PersonDetails>> forFireList = mainService.getPersonsLivingAtThisCoveringAddress(address);

		logger.info("Return list of inhabitants living at the address " + address + " as " + forFireList);

		return ResponseEntity.ok(FilterUtil.applyPersonDetailsExcludeFilter(forFireList, "firstName", "address", "city", "zip", "email"));
	}





	// ========== Endpoint url 5 ===============================================
	// http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	// =========================================================================
	/**
	 * This URL should return a list of all homes served by the requested fire station numbers.
	 * 
	 * @param stations
	 * @return floodStationsList
	 */
	@GetMapping("/flood/stations")
	public ResponseEntity<MappingJacksonValue> getListOfHomesCoveredByTheseStations(@RequestParam List<String> stations) {
		logger.info("GET request /flood/stations?stations=" + stations);

		if (stations.isEmpty()) {
			logger.error("The parameter 'stations' must be set", new Exception("The stations parameter cant't be empty"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		Map<String, List<PersonDetails>> floodStationsList = mainService.getFloodStationsCovering(stations);

		logger.info("Return list of households covered by stations " + stations + " as " + floodStationsList);

		return ResponseEntity.ok(FilterUtil.applyPersonDetailsExcludeFilter(floodStationsList, "firstName", "address", "city", "zip", "email"));
	}





	// ========== Endpoint url 6 ====================================================
	// GET http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	// ==============================================================================
	/**
	 * This URL must return the name, address, age, email and medical history (medics, allergies) of each inhabitant.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return personInfosList The list of personal information of persons with the firstName and lastName passed as parameters
	 */
	@GetMapping("/personInfo")
	public ResponseEntity<MappingJacksonValue> getPersonInfos(@RequestParam String firstName, @RequestParam String lastName) {
		logger.info("GET request /personInfo?firstName=" + firstName + "&lastName=" + lastName);

		if (firstName.isBlank() || lastName.isBlank()) {
			logger.error("The parameter 'firstName' and 'lastName' must be set", new Exception("The firstName parameter and lastName parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List<PersonDetails> personInfosList = mainService.getInfoOfPerson(firstName, lastName);

		if (personInfosList.isEmpty()) {
			logger.error("No data found for " + firstName + " " + lastName, new Exception("Nothing found for this firstName parameter and lastName parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		logger.info("Return info of person " + firstName + " " + lastName + " as " + personInfosList);

		return ResponseEntity.ok(FilterUtil.applyPersonDetailsExcludeFilter(personInfosList, "firstName", "city", "zip", "phone"));
	}





	// ========== Endpoint url 7 ==========================
	// GET http://localhost:8080/communityEmail?city=<city>
	// ====================================================
	/**
	 * This URL must return the email addresses of all the inhabitants of the city.
	 * 
	 * @param city
	 * @return emailsList The list of e-mails all the inhabitants of the city passed as a parameter
	 */
	@GetMapping("/communityEmail")
	public ResponseEntity<List<String>> getCommunityEmails(@RequestParam String city) {
		logger.info("GET request /communityEmail?city=" + city);

		if (city.isBlank()) {
			logger.error("The parameter 'city' must be set", new Exception("The city parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List<String> emailsList = mainService.getAllEmailsForCity(city);

		if (emailsList.isEmpty()) {
			logger.error("No data found for city " + city, new Exception("Nothing found for this city parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		logger.info("Return emails list of all inhabitants of city " + city + " as " + emailsList);

		return ResponseEntity.ok(emailsList);
	}

}
