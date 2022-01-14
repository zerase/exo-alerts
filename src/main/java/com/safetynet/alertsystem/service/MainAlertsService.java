package com.safetynet.alertsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.model.PersonDetails;
import com.safetynet.alertsystem.repository.FireStationRepository;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;
import com.safetynet.alertsystem.util.CalculateUtil;
import com.safetynet.alertsystem.util.ConstantsUtil;

@Service
public class MainAlertsService {

	private static final Logger logger = LogManager.getLogger(MainAlertsService.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	
	// ========== Endpoint url 7 ======================
	// http://localhost:8080/communityEmail?city=<city>
	// ================================================
	public List<String> getAllEmailsForCity(String cityName) {
		logger.debug("Entered into MainService.getAllEmailsForCity method");

		List<Person> personsLivingInThisCity = personRepository.findPersonByCity(cityName);
		List<String> emailsListOfPersonsLivingInThisCity = new ArrayList<String>();

		if (personsLivingInThisCity.isEmpty()) {
			return emailsListOfPersonsLivingInThisCity;
		}

		for (Person person : personsLivingInThisCity) {
			String currentPersonEmail = person.getEmail();
			// Check if email is not already added to avoid duplicates
			if (!emailsListOfPersonsLivingInThisCity.contains(currentPersonEmail)) {
				emailsListOfPersonsLivingInThisCity.add(currentPersonEmail);
			}
		}

		logger.debug("Leaving MainService.getAllEmailsForCity method");

		return emailsListOfPersonsLivingInThisCity;
	}
	
	
	
	
	
	// ========== Endpoint url 3 =======================================
	// http://localhost:8080/phoneAlert?firestation=<firestation_number>
	// =================================================================
	public List<String> getPhonesOfPersonsCoveredByFireStation(String firestationNumber) {
		logger.debug("Entered into MainService.getPhonesOfPersonsCoveredByFireStation method");

		List<FireStation> addressesCoveredByStationNumber = fireStationRepository.findFireStationByStation(firestationNumber);
		List<String> phonesOfPersonsCoveredByThisFireStation = new ArrayList<String>();

		if (addressesCoveredByStationNumber.isEmpty()) {
			return phonesOfPersonsCoveredByThisFireStation;
		}

		for (FireStation fireStation : addressesCoveredByStationNumber) {
			List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
			for (Person person : personsLivingAtThisAddress) {
				String currentPersonPhone = person.getPhone();
				// Check if phone is not already added to avoid duplicates
				if (!phonesOfPersonsCoveredByThisFireStation.contains(currentPersonPhone)) {
					phonesOfPersonsCoveredByThisFireStation.add(currentPersonPhone);
				}
			}
		}

		logger.debug("Leaving MainService.getPhonesOfPersonsCoveredByFireStation method");

		return phonesOfPersonsCoveredByThisFireStation;
	}
	
	
	
	
	
	// ========== Endpoint url 6 ================================================
	// http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	// ==========================================================================
	public List<PersonDetails> getInfoOfPerson(String firstName, String lastName) {
		logger.debug("Entered into MainService.getInfoOfPerson method");

		List<Person> personsWithTheSameLastName = personRepository.findPersonByLastName(lastName);
		List<PersonDetails> personalInfosOfThesePersons = new ArrayList<PersonDetails>();

		if (personsWithTheSameLastName.isEmpty()) {
			return personalInfosOfThesePersons;
		}

		for (Person person : personsWithTheSameLastName) {
			PersonDetails personToGetInfoFrom = new PersonDetails();
			
			MedicalRecord medicalRecordOfCurrentPerson = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(person.getFirstName(), person.getLastName());

			personToGetInfoFrom.setFirstName(person.getFirstName());
			personToGetInfoFrom.setLastName(person.getLastName());
			personToGetInfoFrom.setAddress(person.getAddress());
			personToGetInfoFrom.setEmail(person.getEmail());
			
			personToGetInfoFrom.setMedications(medicalRecordOfCurrentPerson.getMedications());
			personToGetInfoFrom.setAllergies(medicalRecordOfCurrentPerson.getAllergies());
			
			personToGetInfoFrom.setAge(CalculateUtil.calculateAgeByBirthDate(medicalRecordOfCurrentPerson.getBirthdate()));

			personalInfosOfThesePersons.add(personToGetInfoFrom);
		}

		logger.debug("Leaving MainService.getInfoOfPerson method");

		return personalInfosOfThesePersons;
	}
	
	
	
	
	
	// ========== Endpoint url 1 ======================================
	// http://localhost:8080/firestation?stationNumber=<station_number>
	// ================================================================
	public Map<Map<String, String>, List<PersonDetails>> getCoveredPersonsOfFireStation(String stationNumber) {

		logger.debug("Entered into MainService.getCoveredPersonsOfFireStation method");

		List<FireStation> fireStationsContainingThisStationNumber = fireStationRepository.findFireStationByStation(stationNumber);
		List<PersonDetails> personsCoveredByThisStationNumber = new ArrayList<PersonDetails>();

		int nbOfAdults = 0;
		int nbOfChildren = 0;

		// Retrieve list of persons covered by fire station number ...
		for (FireStation fireStation : fireStationsContainingThisStationNumber) {
			
			List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
			
			for (Person person : personsLivingAtThisAddress) {
				
				MedicalRecord medicalRecordOfCurrentPersonCoveredByFireStation = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(person.getFirstName(), person.getLastName());
				PersonDetails personCoveredByFireStation = new PersonDetails();
				
				personCoveredByFireStation.setFirstName(person.getFirstName());
				personCoveredByFireStation.setLastName(person.getLastName());
				personCoveredByFireStation.setAddress(person.getAddress());
				personCoveredByFireStation.setPhone(person.getPhone());
				personCoveredByFireStation.setAge(CalculateUtil.calculateAgeByBirthDate(medicalRecordOfCurrentPersonCoveredByFireStation.getBirthdate()));
				
				personsCoveredByThisStationNumber.add(personCoveredByFireStation);

			}
			
		}
		
		// ... then count number of adults and number of children from this list
		for (PersonDetails personDetails : personsCoveredByThisStationNumber) {
			
			MedicalRecord medicalRecordOfCurrentPersonDetails = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(personDetails.getFirstName(), personDetails.getLastName());

			if(medicalRecordOfCurrentPersonDetails != null) {
				int age = CalculateUtil.calculateAgeByBirthDate(medicalRecordOfCurrentPersonDetails.getBirthdate());
				if(age <= ConstantsUtil.AGE_MAX_OF_CHILDREN) {
					nbOfChildren++;
				} else {
					nbOfAdults++;
				}
			}
			
		}
	
		Map<String, String> numberOfAdultsAndChildrenMap = new HashMap<String, String>();
		numberOfAdultsAndChildrenMap.put("NumberOfAdults", String.valueOf(nbOfAdults));
		numberOfAdultsAndChildrenMap.put("NumberOfChildren", String.valueOf(nbOfChildren));
		
		Map<Map<String, String>, List<PersonDetails>> firestationDetailsResponse = new HashMap<Map<String, String>, List<PersonDetails>>();
		firestationDetailsResponse.put(numberOfAdultsAndChildrenMap, personsCoveredByThisStationNumber);
		
		logger.debug("Leaving MainService.getCoveredPersonsOfFireStation method");

		return firestationDetailsResponse;
	}
	

	

	
	// ========== Endpoint url 2 ========================
	// http://localhost:8080/childAlert?address=<address>
	// ==================================================
	public List<List<PersonDetails>> getChildrenLivingAtAddress(String address) {
		
		logger.debug("Entered into MainService.getChildrenLivingAtAddress method");

		List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(address);
		List<PersonDetails> childrenList = new ArrayList<>();
		List<PersonDetails> otherHouseholdMembers = new ArrayList<>();
		
		for(Person person : personsLivingAtThisAddress) {
			
			PersonDetails childDetails = new PersonDetails();
			PersonDetails otherMemberDetails = new PersonDetails();
			
			MedicalRecord medicalRecordOfCurrentPerson = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(person.getFirstName(), person.getLastName());
			int age = CalculateUtil.calculateAgeByBirthDate(medicalRecordOfCurrentPerson.getBirthdate());
			
			if(age <= ConstantsUtil.AGE_MAX_OF_CHILDREN) {
				
				childDetails.setFirstName(person.getFirstName());
				childDetails.setLastName(person.getLastName());
				childDetails.setAge(age);
				
				childrenList.add(childDetails);
				
			} else {
				
				otherMemberDetails.setFirstName(person.getFirstName());
				otherMemberDetails.setLastName(person.getLastName());
				otherMemberDetails.setAge(age);
				
				otherHouseholdMembers.add(otherMemberDetails);			
			}
		}
		
		List<List<PersonDetails>> childAlertDetailsResponse = new ArrayList<>();
		childAlertDetailsResponse.add(childrenList);
		childAlertDetailsResponse.add(otherHouseholdMembers);
		
		logger.debug("Leaving MainService.getChildrenLivingAtAddress method");
		
		return childAlertDetailsResponse;
	}
	
	
	
	
	
	// ========== Endpoint url 4 ==================
	// http://localhost:8080/fire?address=<address>
	// ============================================
	public Map<Set<String>, List<PersonDetails>> getPersonsLivingAtThisCoveringAddress(String address) {
		
		logger.debug("Entered into MainService.getPersonsLivingAtThisCoveringAddress method");
	
		Set<String> stationsNumberCoveringThisAddress = new HashSet<>();
		List<PersonDetails> personsDetailsForThisAddress = new ArrayList<>();
		
		
		List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(address);
		
		for (Person psn : personsLivingAtThisAddress) {
			
			PersonDetails personLivingAtThisAddress = new PersonDetails();
			personLivingAtThisAddress.setFirstName(psn.getFirstName());
			personLivingAtThisAddress.setLastName(psn.getLastName());
			personLivingAtThisAddress.setPhone(psn.getPhone());
			personLivingAtThisAddress.setAddress(psn.getAddress());
			
			
			MedicalRecord medicalRecordOfCurrentPerson = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(psn.getFirstName(), psn.getLastName());
			
			if (medicalRecordOfCurrentPerson != null) {			
				personLivingAtThisAddress.setAge(CalculateUtil.calculateAgeByBirthDate(medicalRecordOfCurrentPerson.getBirthdate()));
				personLivingAtThisAddress.setMedications(medicalRecordOfCurrentPerson.getMedications());
				personLivingAtThisAddress.setAllergies(medicalRecordOfCurrentPerson.getAllergies());
			}
			
			
			List<FireStation> fireStationsCoveringThisAddress = fireStationRepository.findDistinctByAddress(psn.getAddress());
			
			if (fireStationsCoveringThisAddress != null) {
				for (FireStation fs : fireStationsCoveringThisAddress) {
					stationsNumberCoveringThisAddress.add("station " + fs.getStation());
				}
			}
			
			personsDetailsForThisAddress.add(personLivingAtThisAddress);
			
		}
		
		Map<Set<String>, List<PersonDetails>> fireDetailsResponse = new HashMap<>();
		fireDetailsResponse.put(stationsNumberCoveringThisAddress, personsDetailsForThisAddress);
		
		logger.debug("Leaving MainService.getPersonsLivingAtThisCoveringAddress method");
		
		return fireDetailsResponse;
	}
	
	
	
	
	
	// ========== Endpoint url 5 ===============================================
	// http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	// =========================================================================
	public Map<String, List<PersonDetails>> getFloodStationsCovering(List<String> stationsNumbers) {
		
		logger.debug("Entered into MainService.floodStations method");
		
		Map<String, List<PersonDetails>> floodPersonsResponse = new HashMap<>();
		
		for (String station : stationsNumbers) {
			List<FireStation> fireStationsContainingThisStationNumber = fireStationRepository.findFireStationByStation(station);
			
			if(!fireStationsContainingThisStationNumber.isEmpty()) {
				for(FireStation fs : fireStationsContainingThisStationNumber) {
					List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(fs.getAddress());
					List<PersonDetails> houseHoldMembersList = getPersonsForFlood(personsLivingAtThisAddress);
					
					floodPersonsResponse.put(fs.getAddress(), houseHoldMembersList);
				}
			}
		}	
		
		logger.debug("Leaving MainService.floodStations method");
		
		return floodPersonsResponse;
	}
	
	
	private List<PersonDetails> getPersonsForFlood(List<Person> personsLivingAtSameAddress) {
		
		List<PersonDetails> personsListForFlood = new ArrayList<>();
		
		for (Person psn : personsLivingAtSameAddress) {
			
			PersonDetails houseHoldMember = new PersonDetails();
			
			MedicalRecord mr = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(psn.getFirstName(), psn.getLastName());
			
			houseHoldMember.setFirstName(psn.getFirstName());
			houseHoldMember.setLastName(psn.getLastName());
			houseHoldMember.setPhone(psn.getPhone());
			houseHoldMember.setAge(CalculateUtil.calculateAgeByBirthDate(mr.getBirthdate()));
			houseHoldMember.setMedications(mr.getMedications());
			houseHoldMember.setAllergies(mr.getAllergies());
			
			personsListForFlood.add(houseHoldMember);
		}
		
		return personsListForFlood;
	}

}
