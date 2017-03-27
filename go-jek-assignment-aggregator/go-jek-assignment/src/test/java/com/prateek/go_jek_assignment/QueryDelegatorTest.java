package com.prateek.go_jek_assignment;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.prateek.go_jek_processor_api.OutputWriter;
import com.prateek.go_jek_processor_api.ParkingDataProcessor;
import com.prateek.go_jek_processor_api.ParkingEntryAndExitProcessor;
import com.prateek.go_jek_processor_api.Ticket;

import org.junit.Assert;

public class QueryDelegatorTest {
	
	private QueryDelegator unit;
	private OutputWriter outputWriter;
	private ParkingDataProcessor parkingDataProcessor;
	private ParkingEntryAndExitProcessor parkingEntryAndExitProcessor;
	private MockOutputWriter mockOutputWriter;
	
	@Before
	public void setUp() {
		mockOutputWriter = new MockOutputWriter();
		outputWriter = mockOutputWriter;
		parkingDataProcessor = new MockParkingDataProcessor();
		parkingEntryAndExitProcessor = new MockParkingEntryAndExitProcessor();
		
		unit = new QueryDelegator(outputWriter);
		unit.setParkingDataProcessor(parkingDataProcessor);
		unit.setParkingEntryAndExitProcessor(parkingEntryAndExitProcessor);
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfTypeOne() {
		String input = "create_parking_lot 6";
		String expectedOutput = "Created a parking lot with" + " " + "6" + " "+ "slots";
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfTypeParkAndParkingIsNotFull() {
		String input = "park KA-01-HH-1234 White";
		String expectedOutput = "Allocated slot number:" + " " + 2;
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfTypeParkAndParkingIsFull() {
		String input = "park KA-01-HH-9999 White";
		String expectedOutput = "Sorry, parking lot is full";
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfTypeLeave() {
		String input = "leave 4";
		String expectedOutput = "Slot number" + " "+ "4" + " " +"is free";
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingRegistrationNumbersForVehicleColorCaseOne() {
		String input = "registration_numbers_for_cars_with_colour White";
		String expectedOutput = "AAAA, BBBB";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingRegistrationNumbersForVehicleColorCaseTwo() {
		String input = "registration_numbers_for_cars_with_colour Black";
		String expectedOutput = "CCCC";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingSlotNumberForGivenRegistrationNumber() {
		String input = "slot_number_for_registration_number KA-01-HH-3141";
		String expectedOutput = "1";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingSlotNumberForGivenRegistrationNumberWhenRegistrationNumberIsNotPresent() {
		String input = "slot_number_for_registration_number MH-04-AY-1111";
		String expectedOutput = "Not found";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingSlotNumbersForGivenVehicleColorCaseOne() {
		String input = "slot_numbers_for_cars_with_colour White";
		String expectedOutput = "1, 3";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingSlotNumbersForGivenVehicleColorCaseTwo() {
		String input = "slot_numbers_for_cars_with_colour Black";
		String expectedOutput = "2";
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	@Test
	public void shouldConfirmOutputIsWrittenForInputOfFindingStatus() {
		String input = "status";
		String newLineOperator = "\n";
		String lineOne = "Slot No." + "     " + "Registration No" + "            " + "Colour" + newLineOperator; 
		
		Ticket[] tickets = new Ticket[2];
		tickets[0] = new Ticket("KA-01-HH-1234", "White", 1);
		tickets[1] = new Ticket("KA-01-HH-9999", "Black", 2);
		
		parkingDataProcessor = new MockParkingDataProcessor(tickets);
		unit.setParkingDataProcessor(parkingDataProcessor);
		
		for (Ticket t : tickets) {
			if (t != null) {
				String line = t.getSlotNumber() + "            " + t.getRegistrationNumber() + "              " + t.getVehicleColour() + newLineOperator;
				lineOne += line;
			}
		}
		
		String expectedOutput = lineOne;
		
		unit.processQuery(input);
		
		Assert.assertEquals(expectedOutput, mockOutputWriter.getContentToWrite());
	}
	
	class MockParkingEntryAndExitProcessor implements ParkingEntryAndExitProcessor {

		public String parkingEntryProcess(String vehicleRegistrationNumber, String colour) {
			if (vehicleRegistrationNumber.equals("KA-01-HH-1234") && colour.equals("White")) {
				return "Allocated slot number:" + " " + 2;
			}
			return "Sorry, parking lot is full";
		}

		public String parkingExitProcess(int slotNumber) {
			return "Slot number" + " "+ slotNumber + " " +"is free";
		}
		
	}
	
	class MockParkingDataProcessor implements ParkingDataProcessor {

		private Ticket[] tickets;
		
		public MockParkingDataProcessor(Ticket[] tickets) {
			this.tickets = tickets;
		}
		
		public MockParkingDataProcessor() {
		}
		
		public int deAllocateSlotNumber(int slotNumber) {
			return 0;
		}

		public int vehicleEntryProcessor(String registrationNumber, String vehicleColor) {
			return 2;
		}

		public Ticket[] fetchCurrentStatus() {
			return this.tickets;
		}

		public List<String> fetchRegistrationNumbersForCarsWithColor(String vehicleColor) {
			List<String> registrationNumbers = new ArrayList<String>();
			if (vehicleColor.equals("White")) {
				String registrationNumberOne = "AAAA";
				String registrationNumberTwo = "BBBB";
				registrationNumbers.add(registrationNumberOne);
				registrationNumbers.add(registrationNumberTwo);
				return registrationNumbers;
			}
			String registrationNumber = "CCCC";
			registrationNumbers.add(registrationNumber);
			return registrationNumbers;
		}

		public List<Integer> fetchSlotNumbersForCarsWithColor(String vehicleColor) {
			List<Integer> slotNumbers = new ArrayList<Integer>();
			if (vehicleColor.equals("White")) {
				int slotNumberOne = 1;
				int slotNumberTwo = 3;
				slotNumbers.add(slotNumberOne);
				slotNumbers.add(slotNumberTwo);
				return slotNumbers;
			}
			int slotNumber = 2;
			slotNumbers.add(slotNumber);
			return slotNumbers;
		}

		public int fetchSlotNumberForRegistrationNumber(String registrationNumber) {
			if (registrationNumber.equals("KA-01-HH-3141")) {
				return 1;
			}
			return -1;
		}
	}
	
	class MockOutputWriter implements OutputWriter {

		private String contentToWrite;
		
		public void writeOutput(String output) {
			this.contentToWrite = output;
		}

		protected String getContentToWrite() {
			return contentToWrite;
		}
	}
}
