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

import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.service.PersonService;

@RestController
public class PersonController {

	private static final Logger logger = LogManager.getLogger(PersonController.class);

	@Autowired
	private PersonService personService;

	// ===================
	//   CRUD Operations
	// ===================

	// CREATE - Add a new person
	@PostMapping("/person")
	public ResponseEntity<?> addNewPerson(@RequestBody Person person) {
		logger.info("POST Request '/person' received");

		if (person == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
			logger.error("PersonController error : FirstName and LastName must be set");
			return new ResponseEntity<>("FirstName and LastName must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<Person> createdPerson = personService.addPerson(person);

		if (createdPerson.isPresent()) {
			logger.info("Person successfully added");
			return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
		} else {
			logger.info("Person already exist");
			return new ResponseEntity<>("Person already exist", HttpStatus.NOT_FOUND);
		}

	}

	// READ - Get all persons
	@GetMapping("/persons")
	public Iterable<Person> getAllPersonsInDb() {
		logger.info("GET Request '/person' received");

		Iterable<Person> personsInDb = personService.getAllPersons();

		return personsInDb;
	}

	// UPDATE - Update an existing person
	@PutMapping("/person")
	public ResponseEntity<?> updateExistingPerson(@RequestBody Person person) {
		logger.info("PUT request '/person' received");

		if (person == null || person.getFirstName().isBlank() || person.getLastName().isBlank()) {
			logger.error("PersonController error : FirstName and LastName must be set");
			return new ResponseEntity<>("FirstName and LastName must be set", HttpStatus.BAD_REQUEST);
		}

		Optional<Person> updatedPerson = personService.updatePerson(person);

		if (updatedPerson.isPresent()) {
			logger.info("Person successfully modified");
			return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
		} else {
			logger.info("Person not found");
			return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
		}
	}

	// DELETE - Delete a person
	@DeleteMapping("/person")
	public ResponseEntity<?> deleteExistingPerson(@RequestParam String firstName, @RequestParam String lastName) {
		logger.info("DELETE request '/person' received");

		if (firstName.isBlank() || lastName.isBlank()) {
			logger.error("PersonController error : FirstName and LastName must be set");
			return new ResponseEntity<>("FirstName and LastName must be set", HttpStatus.BAD_REQUEST);
		}

		boolean deletedPerson = personService.deletePerson(firstName, lastName);

		if (deletedPerson == true) {
			logger.info("Person " + firstName + " " + lastName + " successfully deleted");
			return new ResponseEntity<>("Deleted " + firstName + " " + lastName + " successfully", HttpStatus.OK);
		} else {
			logger.info("Person " + firstName + " " + lastName + " not found");
			return new ResponseEntity<>("Person not found", HttpStatus.NOT_FOUND);
		}
	}

}
