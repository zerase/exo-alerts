package com.safetynet.alertsystem.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsystem.model.ChildAlert;
import com.safetynet.alertsystem.model.ChildAlertResponse;
import com.safetynet.alertsystem.model.FireStation;
import com.safetynet.alertsystem.model.FireStationPersonList;
import com.safetynet.alertsystem.model.Household;
import com.safetynet.alertsystem.model.HouseholdResponse;
import com.safetynet.alertsystem.model.MedicalRecord;
import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.model.PersonCoveredByFireStation;
import com.safetynet.alertsystem.model.PersonForFireStationAddress;
import com.safetynet.alertsystem.model.PersonForFireStationAddressResponse;
import com.safetynet.alertsystem.model.PersonForFlood;
import com.safetynet.alertsystem.model.PersonInfo;
import com.safetynet.alertsystem.repository.FireStationRepository;
import com.safetynet.alertsystem.repository.MedicalRecordRepository;
import com.safetynet.alertsystem.repository.PersonRepository;

@Service
public class MainAlertsService {

	private static final Logger logger = LogManager.getLogger(MainAlertsService.class);

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private FireStationRepository fireStationRepository;

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	@Autowired
	private MedicalRecordService medicalRecordService;

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
			if (!emailsListOfPersonsLivingInThisCity.contains(currentPersonEmail)) { // check email not already added
				emailsListOfPersonsLivingInThisCity.add(currentPersonEmail); // add email
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

		List<FireStation> fireStationsInfosOfThisStation = fireStationRepository
				.findFireStationByStation(firestationNumber);
		List<String> phonesOfPersonsCoveredByThisFireStation = new ArrayList<String>();

		if (fireStationsInfosOfThisStation.isEmpty()) {
			return phonesOfPersonsCoveredByThisFireStation;
		}

		for (FireStation fireStation : fireStationsInfosOfThisStation) {
			List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(fireStation.getAddress());
			for (Person person : personsLivingAtThisAddress) {
				String currentPersonPhone = person.getPhone();
				if (!phonesOfPersonsCoveredByThisFireStation.contains(currentPersonPhone)) { // check phone not already
																								// added
					phonesOfPersonsCoveredByThisFireStation.add(currentPersonPhone); // add phone
				}
			}
		}

		logger.debug("Leaving MainService.getPhonesOfPersonsCoveredByFireStation method");

		return phonesOfPersonsCoveredByThisFireStation;
	}

	// ========== Endpoint url 6 ================================================
	// http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
	// ==========================================================================
	public List<PersonInfo> getInfoOfPerson(String firstName, String lastName) {

		logger.debug("Entered into MainService.getInfoOfPerson method");

		List<Person> personsWithThisFullName = personRepository.findPersonInfoByFirstNameAndLastName(firstName,
				lastName);
		List<PersonInfo> personInfoOfThisPerson = new ArrayList<PersonInfo>();

		if (personsWithThisFullName.isEmpty()) {
			return personInfoOfThisPerson;
		}

		for (Person p : personsWithThisFullName) {
			PersonInfo personInfoToGetFrom = new PersonInfo();
			MedicalRecord medicalRecordInfoOfThisPerson = null;
			int age;
			List<String> medications;
			List<String> allergies;

			medicalRecordInfoOfThisPerson = medicalRecordService.getMedicalRecordInfoOfPerson(p.getFirstName(),
					p.getLastName());
			age = medicalRecordService.getAgeOf(p.getFirstName(), p.getLastName());
			medications = medicalRecordInfoOfThisPerson.getMedications();
			allergies = medicalRecordInfoOfThisPerson.getAllergies();

			personInfoToGetFrom.setFirstName(p.getFirstName());
			personInfoToGetFrom.setLastName(p.getLastName());
			personInfoToGetFrom.setAddress(p.getAddress());
			personInfoToGetFrom.setAge(age);
			personInfoToGetFrom.setEmail(p.getEmail());
			personInfoToGetFrom.setMedications(medications);
			personInfoToGetFrom.setAllergies(allergies);

			personInfoOfThisPerson.add(personInfoToGetFrom);
		}

		logger.debug("Leaving MainService.getInfoOfPerson method");

		return personInfoOfThisPerson;

	}

	// ========== Endpoint url 1 ======================================
	// http://localhost:8080/firestation?stationNumber=<station_number>
	// ================================================================
	public FireStationPersonList getCoveredPersonsOfFireStation(String stationNumber) {

		logger.debug("Entered into MainService.getCoveredPersonsOfFireStation method");

		List<FireStation> fireStationsContainingThisStationNumber = fireStationRepository
				.findFireStationByStation(stationNumber);
		List<PersonCoveredByFireStation> personsListCoveredByThisStationNumber = new ArrayList<>();

		MedicalRecord medicalRecordOfPersonCoveredByFireStation;
		int nbOfAdults = 0;
		int nbOfChildren = 0;
		int age;

		FireStationPersonList fireStationPersonListCoveredByStation = new FireStationPersonList();

		if (fireStationsContainingThisStationNumber.isEmpty()) {
			return fireStationPersonListCoveredByStation;
		}

		// Retrieve list of persons covered by fire station number ...
		for (FireStation fs : fireStationsContainingThisStationNumber) {
			List<Person> personsLivingAtThisAddress = personRepository.findPersonByAddress(fs.getAddress());
			for (Person psn : personsLivingAtThisAddress) {
				PersonCoveredByFireStation personCoveredByFireStation = new PersonCoveredByFireStation();
				personCoveredByFireStation.setFirstName(psn.getFirstName());
				personCoveredByFireStation.setLastName(psn.getLastName());
				personCoveredByFireStation.setAddress(psn.getAddress());
				personCoveredByFireStation.setPhone(psn.getPhone());

				if (!personsListCoveredByThisStationNumber.contains(personCoveredByFireStation)) { // check if not
																									// already added
					personsListCoveredByThisStationNumber.add(personCoveredByFireStation); // add it
				}

			}
		}

		// ... then check for each person covered by this fire station number list if
		// it's an adult (more than 19) or a child
		for (PersonCoveredByFireStation p : personsListCoveredByThisStationNumber) {
			medicalRecordOfPersonCoveredByFireStation = medicalRecordService
					.getMedicalRecordInfoOfPerson(p.getFirstName(), p.getLastName());
			if (medicalRecordOfPersonCoveredByFireStation != null) {
				age = medicalRecordService.getAgeOf(p.getFirstName(), p.getLastName());
				if (age > 19) {
					nbOfAdults++;
				} else {
					nbOfChildren++;
				}
			}
		}

		fireStationPersonListCoveredByStation.setNbOfAdults(nbOfAdults);
		fireStationPersonListCoveredByStation.setNbOfChildren(nbOfChildren);
		fireStationPersonListCoveredByStation.setPersonsCoveredByFireStation(personsListCoveredByThisStationNumber);

		logger.debug("Leaving MainService.getCoveredPersonsOfFireStation method");

		return fireStationPersonListCoveredByStation;
	}

	// ========== Endpoint url 2 ========================
	// http://localhost:8080/childAlert?address=<address>
	// ==================================================
	public ChildAlertResponse getChildrenLivingAtAddress(String address) {
		// Recherche des enfants
		ChildAlertResponse childAlertResponse = new ChildAlertResponse();
		List<ChildAlert> childAlerts = new ArrayList<>();
		List<Person> otherPersonsLivingAtThisAddress;
		ChildAlert childAlert;

		// Recherche des medical records de même nom/prenom et <= 18 ans
		for (MedicalRecord mr : medicalRecordRepository.findAll()) {
			if (medicalRecordService.countAgeOf(mr) < 19
					&& personRepository.findPersonByFirstNameAndLastName(mr.getFirstName(), mr.getLastName()) != null
					&& personRepository.findPersonByFirstNameAndLastName(mr.getFirstName(), mr.getLastName()).get()
							.getAddress().equals(address)) {
				otherPersonsLivingAtThisAddress = new ArrayList<>();
				childAlert = new ChildAlert();
				childAlert.setFirstName(mr.getFirstName());
				childAlert.setLastName(mr.getLastName());
				childAlert.setAge(medicalRecordService.countAgeOf(mr));

				// Recherche des membres habitants à cette adresse
				for (Person psn : personRepository.findAll()) {
					if (psn.getAddress().equals(address) && !(psn.getFirstName().equals(mr.getFirstName())
							&& psn.getLastName().equals(mr.getLastName()))) {
						otherPersonsLivingAtThisAddress.add(psn);
					}
				}
				childAlert.setPersons(otherPersonsLivingAtThisAddress);
				childAlerts.add(childAlert);
			}
		}
		childAlertResponse.setChildAlerts(childAlerts);
		return childAlertResponse;
	}

	// ========== Endpoint url 4 ==================
	// http://localhost:8080/fire?address=<address>
	// ============================================
	public PersonForFireStationAddressResponse fire(String address) {
		PersonForFireStationAddressResponse personForFireStationAddressResponse = new PersonForFireStationAddressResponse();
		List<PersonForFireStationAddress> listPersonsForFireStationAddress = new ArrayList<>();
		FireStation fireStation;
		MedicalRecord medicalRecord;
		PersonForFireStationAddress personForFireStationAddress;
		int age;
		for (Person psn : personRepository.findPersonByAddress(address)) {
			personForFireStationAddress = new PersonForFireStationAddress();
			personForFireStationAddress.setFirstname(psn.getFirstName());
			personForFireStationAddress.setLastname(psn.getLastName());
			personForFireStationAddress.setAddress(psn.getAddress());
			personForFireStationAddress.setPhone(psn.getPhone());
			fireStation = fireStationRepository.findFsByAddress(psn.getAddress());
			if (fireStation != null) {
				personForFireStationAddress.setStation(fireStation.getStation());
			}
			medicalRecord = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(psn.getFirstName(),
					psn.getLastName());
			if (medicalRecord != null) {
				age = medicalRecordService.countAgeOf(medicalRecord);
				personForFireStationAddress.setAge(age);
				personForFireStationAddress.setMedications(medicalRecord.getMedications());
				personForFireStationAddress.setAllergies(medicalRecord.getAllergies());
			}
			listPersonsForFireStationAddress.add(personForFireStationAddress);
		}
		personForFireStationAddressResponse.setPersonForFireStationAddress(listPersonsForFireStationAddress);
		return personForFireStationAddressResponse;
	}

	// ========== Endpoint url 5 ===============================================
	// http://localhost:8080/flood/stations?stations=<a list of station_numbers>
	// =========================================================================
	public HouseholdResponse floodStations(List<String> stations) {
		HouseholdResponse householdresponse = new HouseholdResponse();
		List<Household> households = new ArrayList<>();
		Household household;
		String address;

		// On charge un Set des adresses des firestations
		Set<String> setAddress = getAddressStations(stations);

		// On parcourt le Set des adresses
		Iterator<String> addr = setAddress.iterator();
		while (addr.hasNext()) {
			address = addr.next();
			List<Person> persons = personRepository.findPersonByAddress(address);
			if (persons != null && persons.size() > 0) {
				household = new Household();
				household.setAddress(address);
				List<PersonForFlood> personForFloods = getPersonForFlood(persons);
				household.setPersonsForFlood(personForFloods);
				households.add(household);
			}
		}
		householdresponse.setHouseholds(households);
		return householdresponse;
	}

	// Chargement dans une set des adresses des firestations (pour éviter les
	// doublons)
	private Set<String> getAddressStations(List<String> stations) {
		logger.debug("getAddressStations");
		Set<String> setAddresses = new HashSet<>();
		for (String station : stations) {
			List<FireStation> address = fireStationRepository.findFireStationByStation(station);
			for (FireStation addr : address) {
				setAddresses.add(addr.getAddress());
			}
		}
		return setAddresses;
	}

	private List<PersonForFlood> getPersonForFlood(List<Person> persons) {
		logger.debug("getPersonForFlood");
		List<PersonForFlood> personsForFlood = new ArrayList<>();
		PersonForFlood personForFlood;
		MedicalRecord medicalRecord;
		for (Person psn : persons) {
			personForFlood = new PersonForFlood();
			personForFlood.setFirstName(psn.getFirstName());
			personForFlood.setLastName(psn.getLastName());
			personForFlood.setPhone(psn.getPhone());
			medicalRecord = medicalRecordRepository.findMedicalRecordInfoByFirstNameAndLastName(psn.getFirstName(),
					psn.getLastName());
			if (medicalRecord != null) {
				personForFlood.setAge(medicalRecordService.countAgeOf(medicalRecord));
				personForFlood.setMedications(medicalRecord.getMedications());
				personForFlood.setAllergies(medicalRecord.getAllergies());
			}
			personsForFlood.add(personForFlood);
		}
		return personsForFlood;
	}

}
