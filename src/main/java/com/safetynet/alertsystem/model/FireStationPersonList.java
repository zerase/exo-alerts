package com.safetynet.alertsystem.model;

import java.util.List;

public class FireStationPersonList {

	int nbOfAdults;
	int nbOfChildren;
	private List<PersonCoveredByFireStation> personsCoveredByFireStation;

	public int getNbOfAdults() {
		return nbOfAdults;
	}

	public void setNbOfAdults(int nbOfAdults) {
		this.nbOfAdults = nbOfAdults;
	}

	public int getNbOfChildren() {
		return nbOfChildren;
	}

	public void setNbOfChildren(int nbOfChildren) {
		this.nbOfChildren = nbOfChildren;
	}

	public List<PersonCoveredByFireStation> getPersonsCoveredByFireStation() {
		return personsCoveredByFireStation;
	}

	public void setPersonsCoveredByFireStation(List<PersonCoveredByFireStation> personsCoveredByFireStation) {
		this.personsCoveredByFireStation = personsCoveredByFireStation;
	}

}
