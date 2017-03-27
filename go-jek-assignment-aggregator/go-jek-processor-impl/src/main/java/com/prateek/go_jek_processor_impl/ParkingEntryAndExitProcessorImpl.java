package com.prateek.go_jek_processor_impl;

import com.prateek.go_jek_processor_api.ParkingDataProcessor;
import com.prateek.go_jek_processor_api.ParkingEntryAndExitProcessor;
import com.prateek.go_jek_processor_api.Ticket;

public class ParkingEntryAndExitProcessorImpl implements ParkingEntryAndExitProcessor {
	
	private ParkingDataProcessor parkingDataProcessor;
	
	public ParkingEntryAndExitProcessorImpl(ParkingDataProcessor parkingDataProcessor) {
		this.parkingDataProcessor = parkingDataProcessor;
	}

	public String parkingEntryProcess(String vehicleRegistrationNumber, String vehicleColour) {
		String output = "";
		int availableParkingSlot = parkingDataProcessor.vehicleEntryProcessor(vehicleRegistrationNumber, vehicleColour);
		if (availableParkingSlot != -1) {
			output = "Allocated slot number:" + " " + availableParkingSlot;
		} else {
			output = "Sorry, parking lot is full";
		}
		return output;
	}

	public String parkingExitProcess(int slotNumber) {
		String output = "";
		int slNumber = parkingDataProcessor.deAllocateSlotNumber(slotNumber);
		if (slNumber == slotNumber) {
			output = "Slot number" + " "+ slotNumber + " " +"is free";
		} else if (slNumber == -1) {
			output = "Unallocated slot number can not be freed";
		} else {
			output = "Slot number can not exceed parking lot capacity";
		}
		return output;
	}
}
