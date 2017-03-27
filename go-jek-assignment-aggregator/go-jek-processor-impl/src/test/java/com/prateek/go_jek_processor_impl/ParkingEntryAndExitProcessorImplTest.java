package com.prateek.go_jek_processor_impl;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.prateek.go_jek_processor_api.ParkingDataProcessor;
import com.prateek.go_jek_processor_api.ParkingEntryAndExitProcessor;
import com.prateek.go_jek_processor_api.Ticket;

import org.junit.Assert;

public class ParkingEntryAndExitProcessorImplTest {
	
	private ParkingEntryAndExitProcessor unit;
	private ParkingDataProcessor parkingDataProcessor;
	private MockParkingDataProcessor parkingDataProcessorTestPurpose;
	
	@Before
	public void setUp() {
		parkingDataProcessorTestPurpose = new MockParkingDataProcessor();
	}
	
	@Test
	public void shouldAllocateProperSlotNumberWhenParkingIsAvailable() {
		int slotNumber = 2;
		parkingDataProcessorTestPurpose.setSlotNumber(slotNumber);
		parkingDataProcessor = parkingDataProcessorTestPurpose;
		
		unit = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
		
		String randomRegistrationNumber = "AAAA";
		String randomVehicleColor = "Blue";
		String expectedMessage = "Allocated slot number:" + " " + slotNumber;
		
		Assert.assertEquals(expectedMessage, unit.parkingEntryProcess(randomRegistrationNumber, randomVehicleColor));
	}
	
	@Test
	public void shouldNotAllocateProperSlotNumberWhenParkingIsFull() {
		int slotNumber = -1;
		parkingDataProcessorTestPurpose.setSlotNumber(slotNumber);
		parkingDataProcessor = parkingDataProcessorTestPurpose;
		
		unit = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
		
		String randomRegistrationNumber = "AAAA";
		String randomVehicleColor = "Blue";
		String expectedMessage = "Sorry, parking lot is full";
		
		Assert.assertEquals(expectedMessage, unit.parkingEntryProcess(randomRegistrationNumber, randomVehicleColor));
	}
	
	@Test
	public void shouldDeAllocateProperSlotNumber() {
		int slotNumber = 2;
		parkingDataProcessorTestPurpose.setSlotNumber(slotNumber);
		parkingDataProcessor = parkingDataProcessorTestPurpose;
		
		unit = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
		
		String expectedMessage = "Slot number" + " "+ slotNumber + " " +"is free";
		
		Assert.assertEquals(expectedMessage, unit.parkingExitProcess(slotNumber));
	}
	
	@Test
	public void shouldNotDeAllocateUnAllocatedSlotNumber() {
		int slotNumber = 2;
		parkingDataProcessorTestPurpose.setSlotNumber(-1);
		parkingDataProcessor = parkingDataProcessorTestPurpose;
		
		unit = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
		
		String expectedMessage = "Unallocated slot number can not be freed";
		
		Assert.assertEquals(expectedMessage, unit.parkingExitProcess(slotNumber));
	}
	
	@Test
	public void shouldNotDeAllocateSlotNumberWhichIsGreaterThanParkingSize() {
		int slotNumber = 5;
		parkingDataProcessorTestPurpose.setSlotNumber(-2);
		parkingDataProcessor = parkingDataProcessorTestPurpose;
		
		unit = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
		
		String expectedMessage = "Slot number can not exceed parking lot capacity";
		
		Assert.assertEquals(expectedMessage, unit.parkingExitProcess(slotNumber));
	}
	
	class MockParkingDataProcessor implements ParkingDataProcessor {
		
		private int slotNumber;

		public int deAllocateSlotNumber(int slotNumber) {
			return this.slotNumber;
		}

		public int vehicleEntryProcessor(String registrationNumber, String vehicleColor) {
			return slotNumber;
		}

		public Ticket[] fetchCurrentStatus() {
			return null;
		}

		public List<String> fetchRegistrationNumbersForCarsWithColor(String vehicleColor) {
			return null;
		}

		public List<Integer> fetchSlotNumbersForCarsWithColor(String vehicleColor) {
			return null;
		}

		public int fetchSlotNumberForRegistrationNumber(String registrationNumber) {
			return 0;
		}

		protected int getSlotNumber() {
			return slotNumber;
		}

		protected void setSlotNumber(int slotNumber) {
			this.slotNumber = slotNumber;
		}
		
	}
}
