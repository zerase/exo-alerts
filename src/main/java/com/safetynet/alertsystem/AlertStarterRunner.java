package com.safetynet.alertsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.safetynet.alertsystem.service.ReaderJsonService;

@Component
public class AlertStarterRunner implements CommandLineRunner {

	@Autowired
	ReaderJsonService readerJsonService;

	@Override
	public void run(String... args) throws Exception {
		// Load data
		readerJsonService.SaveJsonDataInDb();

	}

}
