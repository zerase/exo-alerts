package com.safetynet.alertsystem.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("myFilterPersonDetails")
public class PersonDetails {
	
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String zip;
	private String phone;
	private String email;
	private int age;
	private List<String> medications;
	private List<String> allergies;

	
	// Constructors
	public PersonDetails() {
		
	}
	
	public PersonDetails(String firstName, String lastName, String address, String city, String zip, String phone, String email, int age, List<String> medications, List<String> allergies) {	
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
		this.age = age;
		this.medications = medications;
		this.allergies = allergies;
	}
	

	// Getters and setters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getMedications() {
		return medications;
	}

	public void setMedications(List<String> medications) {
		this.medications = medications;
	}

	public List<String> getAllergies() {
		return allergies;
	}

	public void setAllergies(List<String> allergies) {
		this.allergies = allergies;
	}

	@Override
	public String toString() {
		return "PersonDetails [firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", city="
				+ city + ", zip=" + zip + ", phone=" + phone + ", email=" + email + ", age=" + age + ", medications="
				+ medications + ", allergies=" + allergies + "]";
	}	

}
