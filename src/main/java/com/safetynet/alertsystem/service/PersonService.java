package com.safetynet.alertsystem.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.alertsystem.model.Person;
import com.safetynet.alertsystem.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger logger = LogManager.getLogger(PersonService.class);

	@Autowired
	private PersonRepository personRepository;

	// GET all
	public Iterable<Person> getAllPersons() {
		try {
			return personRepository.findAll();
		} catch (Exception e) {
			logger.error("Error attempting to get persons", e);
		}
		return null;
	}

	// POST
	public Optional<Person> addPerson(Person person) {
		try {
			Optional<Person> personToCreate = personRepository.findPersonByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());
			if (personToCreate.isPresent()) {
				return Optional.empty();
			}
			personRepository.save(person);
		} catch (Exception e) {
			logger.error("Error attempting to add a new person", e);
		}
		return Optional.of(person);
	}

	// PUT
	public Optional<Person> updatePerson(Person person) {
		try {
			Optional<Person> personToUpdate = personRepository.findPersonByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());

			if (personToUpdate.isPresent()) {
				Person personUpdated = personToUpdate.get();

				personUpdated.setAddress(person.getAddress());
				personUpdated.setCity(person.getCity());
				personUpdated.setZip(person.getZip());
				personUpdated.setPhone(person.getPhone());
				personUpdated.setEmail(person.getEmail());

				personRepository.save(personUpdated);
				return Optional.of(personUpdated);
			}
		} catch (Exception e) {
			logger.error("Error attempting to update a person", e);
		}
		return Optional.empty();
	}

	// DELETE
	public boolean deletePerson(String firstName, String lastName) {
		try {
			Optional<Person> personToDelete = personRepository.findPersonByFirstNameAndLastName(firstName, lastName);
			if (personToDelete.isPresent()) {
				Person personDeleted = personToDelete.get();
				personRepository.delete(personDeleted);
				return true;
			}
		} catch (Exception e) {
			logger.error("Error attempting to delete a person", e);
		}
		return false;
	}

}
