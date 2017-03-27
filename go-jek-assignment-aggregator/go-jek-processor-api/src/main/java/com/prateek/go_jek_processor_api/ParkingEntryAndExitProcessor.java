package com.prateek.go_jek_processor_api;

public interface ParkingEntryAndExitProcessor {

	String parkingEntryProcess(String vehicleRegistrationNumber, String colour);
	String parkingExitProcess(int slotNumber);
}
