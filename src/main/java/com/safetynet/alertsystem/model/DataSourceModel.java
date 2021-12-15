package com.safetynet.alertsystem.model;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class DataSourceModel {

	private ArrayList<Person> persons;
	private ArrayList<FireStation> firestations;
	private ArrayList<MedicalRecord> medicalrecords;

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public void setPersons(ArrayList<Person> persons) {
		this.persons = persons;
	}

	public ArrayList<FireStation> getFirestations() {
		return firestations;
	}

	public void setFirestations(ArrayList<FireStation> firestations) {
		this.firestations = firestations;
	}

	public ArrayList<MedicalRecord> getMedicalrecords() {
		return medicalrecords;
	}

	public void setMedicalrecords(ArrayList<MedicalRecord> medicalrecords) {
		this.medicalrecords = medicalrecords;
	}

}
