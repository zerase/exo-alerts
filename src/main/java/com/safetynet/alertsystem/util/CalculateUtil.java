package com.safetynet.alertsystem.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class CalculateUtil {
	
	public static int calculateAgeByBirthDate(String birthdate) {
		
		LocalDate birthDate = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		
		Period agePeriod = Period.between(birthDate, LocalDate.now());
		return agePeriod.getYears();
	}

}
