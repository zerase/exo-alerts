package com.safetynet.alertsystem.model;

import java.util.List;

public class Household {

	private String address;
	private List<PersonForFlood> personsForFlood;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<PersonForFlood> getPersonsForFlood() {
		return personsForFlood;
	}

	public void setPersonsForFlood(List<PersonForFlood> personsForFlood) {
		this.personsForFlood = personsForFlood;
	}

}
