package com.safetynet.alertsystem.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.safetynet.alertsystem.model.ChildAlertResponse;
import com.safetynet.alertsystem.model.FireStationPersonList;
import com.safetynet.alertsystem.model.HouseholdResponse;
import com.safetynet.alertsystem.model.PersonCoveredByFireStation;
import com.safetynet.alertsystem.model.PersonForFireStationAddressResponse;
import com.safetynet.alertsystem.model.PersonInfo;
import com.safetynet.alertsystem.service.MainAlertsService;


@RestController
public class MainAlertsController {
	
	private static final Logger logger = LogManager.getLogger(MainAlertsController.class);

	@Autowired
	private MainAlertsService mainService;
	
	
	// ========== Endpoint url 1 ====================================================================================
	// GET http://localhost:8080/firestation?stationNumber=<station_number>
	// This url should return a list of people covered by the corresponding fire station.
	// So, if the station number = 1, it must return the inhabitants covered by the station number 1. 
	// The list must include the following specific information: 
	// 		- first name 
	// 		- last name 
	// 		- address
	// 		- telephone number 
	// What's more, it must provide a count of the number of adults and the number of children (anyone aged 18 or over
	// less) in the service area.
	// ==============================================================================================================
	@GetMapping("/firestation")
	public FireStationPersonList getPersonsListByFireStationCovering(@RequestParam String stationNumber){
		logger.info("GET request /firestation?stationNumber=" + stationNumber);
		
		if(stationNumber.isBlank()) {
			logger.error("The parameter 'stationNumber' must be set", new Exception("The stationNumber parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		FireStationPersonList personsCoveredByFireStation = mainService.getCoveredPersonsOfFireStation(stationNumber);
		
		List<PersonCoveredByFireStation> checkPersonsCoveredByFireStationReturnValue = personsCoveredByFireStation.getPersonsCoveredByFireStation();
		if(checkPersonsCoveredByFireStationReturnValue != null) {
			logger.info("Return list of persons covered by firestation number " + stationNumber);
			return personsCoveredByFireStation;
		} else {
			logger.error("No data found for stationNumber=" + stationNumber, new Exception("Nothing found for this stationNumber parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	
	
	
	// ========== Endpoint url 2 ===============================================
	// GET http://localhost:8080/childAlert?address=<address>
	// This url should return a list of children (anyone aged 18 or under) 
	// living at this address.
	// The list should include for each child: 
	// 		- first name
	// 		- last name 
	// 		- age
	// 		- a list of others household members
	// If there is no child, this url may return an empty string.
	// =========================================================================	
	@GetMapping("/childAlert")
	public ChildAlertResponse getChildrenList(@RequestParam String address){
		logger.info("GET request /childAlert?address=" + address);
		
		if(address.isBlank()) {
			logger.error("The parameter 'address' must be set", new Exception("The address parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		ChildAlertResponse childrenList = mainService.getChildrenLivingAtAddress(address);
		
		if(childrenList != null) {
			logger.info("Return list of children living at the address " + address);
			return childrenList;
		} else {
			logger.error("No data found for address=" + address, new Exception("Nothing found for this address parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
		
		
		
		
	
	// ========== Endpoint url 3 ===============================================
	// GET http://localhost:8080/phoneAlert?firestation=<firestation_number>
	// This url must return a list of the telephone numbers of the residents 
	// served by the fire station.
	// We will use it to send emergency text messages to specific households.
	// =========================================================================
	@GetMapping("/phoneAlert")
	public List<String> getPhonesAlert(@RequestParam String firestation){
		logger.info("GET request /phoneAlert?firestation=" + firestation);
		
		if(firestation.isBlank()) {
			logger.error("The parameter 'firestation' must be set", new Exception("The firestation parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		List<String> phonesList = mainService.getPhonesOfPersonsCoveredByFireStation(firestation);
		
		if(phonesList.isEmpty()) {
			logger.error("No data found for firestation=" + firestation, new Exception("Nothing found for this firestation parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		logger.info("Return phones list of inhabitants covered by firestation " + firestation + " : " + phonesList);
		
		return phonesList;
	}
	
	
	
	
	
	// ========== Endpoint url 4 ===============================================
	// GET http://localhost:8080/fire?address=<address>
	// This url must return the list of inhabitants living at the given address 
	// as well as the number of the fire station serving it. 
	// The list should include for each person :
	// 		- name 
	// 		- phone number 
	// 		- age
	// 		- medical history (medications, dosage and allergies)
	// =========================================================================
	@GetMapping("/fire")
	public PersonForFireStationAddressResponse getPersonsForFireStationAddress(@RequestParam String address){
		logger.info("GET request /fire?address=" + address);
		
		if(address.isBlank()) {
			logger.error("The parameter 'address' must be set", new Exception("The address parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		PersonForFireStationAddressResponse fireList = mainService.fire(address);
		
		if(fireList != null) {
			logger.info("Return list of inhabitants living at the address " + address);
			return fireList;
		} else {
			logger.error("No data found for address=" + address, new Exception("Nothing found for this address parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	
	// ========== Endpoint url 5 ===============================================
	// http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	// This url should return a list of all homes served by the fire station. 
	// This list should include the people by address. It must also include : 
	// 		- name
	// 		- telephone number
	// 		- age
	// 		- medical history (medications, allergies)
	// of each inhabitant.
	// =========================================================================
	@GetMapping("/flood/stations")
	public HouseholdResponse floodStations(@RequestParam List<String> stations){
		logger.info("GET request /flood/stations?stations=" + stations);
		
		if(stations.isEmpty()) {
			logger.error("The parameter 'stations' must be set", new Exception("The stations parameter cant't be empty"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		HouseholdResponse floodStationsList = mainService.floodStations(stations);
		
		if(floodStationsList != null) {
			logger.info("Return list of households covered by stations " + stations);
			return floodStationsList;
		} else {
			logger.error("No data found for stations=" + stations, new Exception("Nothing found for this stations parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	
	
	
	
	// ========== Endpoint url 6 ===============================================
	// GET http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	// This url must return : 
	// 		- name
	// 		- address
	// 		- age
	// 		- email
	// 		- medical history (medications, allergies) 
	// of each inhabitant.
	// =========================================================================
	@GetMapping("/personInfo")
	public List<PersonInfo> getPersonInfos(@RequestParam String firstName, @RequestParam String lastName){
		logger.info("GET request /personInfo?firstName=" + firstName + "&lastName=" + lastName);
		
		if(firstName.isBlank() || lastName.isBlank()) {
			logger.error("The parameter 'firstName' and 'lastName' must be set", new Exception("The firstName parameter and lastName parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		List<PersonInfo> personInfosList = mainService.getInfoOfPerson(firstName, lastName);
		
		if(personInfosList.isEmpty()) {
			logger.error("No data found for " + firstName + " " + lastName, new Exception("Nothing found for this firstName parameter and lastName parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		logger.info("Return info of person " + firstName + " " + lastName);
		
		return personInfosList;
	}
	
	
	
	
	
	// ========== Endpoint url 7 ===============================================
	// GET http://localhost:8080/communityEmail?city=<city>
	// This url must return emails of all the inhabitants of the city.
	// =========================================================================
	@GetMapping("/communityEmail")
	public List<String> getCommunityEmails(@RequestParam String city) {
		logger.info("GET request /communityEmail?city=" + city);
		
		if(city.isBlank()) {
			logger.error("The parameter 'city' must be set", new Exception("The city parameter cant't be blank"));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		List<String> emailsList = mainService.getAllEmailsForCity(city);
		
		
		if(emailsList.isEmpty()) {
			logger.error("No data found for city " + city, new Exception("Nothing found for this city parameter"));
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		logger.info("Return emails list of all inhabitants of " + city +  " : " + emailsList );
			
		return emailsList;
	}

}
