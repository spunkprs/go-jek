package com.prateek.go_jek_processor_api;

import java.util.List;

public interface ParkingDataProcessor {

	int deAllocateSlotNumber(int slotNumber);
	int vehicleEntryProcessor(String registrationNumber, String vehicleColor);
	Ticket[] fetchCurrentStatus();
	List<String> fetchRegistrationNumbersForCarsWithColor(String vehicleColor);
	List<Integer> fetchSlotNumbersForCarsWithColor(String vehicleColor);
	int fetchSlotNumberForRegistrationNumber(String registrationNumber);
}
