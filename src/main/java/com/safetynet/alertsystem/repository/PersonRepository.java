package com.safetynet.alertsystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.safetynet.alertsystem.model.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

	Optional<Person> findPersonByFirstNameAndLastName(String firstName, String lastName);

	// for endpoint 7
	List<Person> findPersonByCity(String city);

	// for endpoint 3 and endpoint 4
	List<Person> findPersonByAddress(String address);

	// for endpoint 6, endpoint 1 and endpoint 5
	List<Person> findPersonInfoByFirstNameAndLastName(String firstName, String lastName);

}
