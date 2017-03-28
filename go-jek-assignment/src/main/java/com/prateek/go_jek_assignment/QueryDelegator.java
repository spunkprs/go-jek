package com.prateek.go_jek_assignment;

import java.util.List;

import com.prateek.go_jek_processor_api.OutputWriter;
import com.prateek.go_jek_processor_api.ParkingDataProcessor;
import com.prateek.go_jek_processor_api.ParkingEntryAndExitProcessor;
import com.prateek.go_jek_processor_api.Queries;
import com.prateek.go_jek_processor_api.Ticket;
import com.prateek.go_jek_processor_impl.ParkingDataProcessorImpl;
import com.prateek.go_jek_processor_impl.ParkingEntryAndExitProcessorImpl;

public class QueryDelegator {
	
	private ParkingDataProcessor parkingDataProcessor;
	private ParkingEntryAndExitProcessor parkingEntryAndExitProcessor;
	private OutputWriter outputWriter;
	private String PARKING_LOT_NOT_CREATED = "Parking lot not created";

	public QueryDelegator(OutputWriter outputWriter) {
		this.outputWriter = outputWriter;
	}
	
	public void processQuery(String query) {
		
		String st[];
		String output = "";
		if (query.contains(Queries.CREATE_PARKING_LOT.getQuery())) {
			st = query.split(" ");
			if (parkingDataProcessor == null) {
				parkingDataProcessor = new ParkingDataProcessorImpl(Integer.parseInt(st[1]));
				parkingEntryAndExitProcessor = new ParkingEntryAndExitProcessorImpl(parkingDataProcessor);
			}
			output = "Created a parking lot with" + " " + st[1] + " "+ "slots";
			writeOutput(output);
		} else if (query.contains("park")) {
			st = query.split(" ");
			String vehicleRegistrationNumber = st[1];
			String vehicleColour = st[2];
			if (isParkingLotCreated()) {
				output = parkingEntryAndExitProcessor.parkingEntryProcess(vehicleRegistrationNumber, vehicleColour);
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		} else if (query.contains(Queries.LEAVE.getQuery())) {
			st = query.split(" ");
			int slotNumber = Integer.parseInt(st[1]);
			if (isParkingLotCreated()) {
				output = parkingEntryAndExitProcessor.parkingExitProcess(slotNumber);
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		} else if (query.equalsIgnoreCase(Queries.STATUS.getQuery())) {
			if (isParkingLotCreated()) {
				Ticket tickets[] = parkingDataProcessor.fetchCurrentStatus();
				if (tickets.length > 0) {
					output = OutputGenerator.generateStatus(tickets);
				} else {
					output = "No status for empty parking";
				}
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		} else if (query.contains(Queries.REGISTRATION_NUMBERS_FOR_CARS_WITH_COLOUR.getQuery())) {
			st = query.split(" ");
			String color = st[1];
			if (isParkingLotCreated()) {
				List<String> registrationNumbers = parkingDataProcessor.fetchRegistrationNumbersForCarsWithColor(color);
				if (!registrationNumbers.isEmpty()) {
					output = OutputGenerator.generateOutputForRegistrationNumbers(registrationNumbers);
				} else {
					output = "No vehicles of color" + " " + color;
				}
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		} else if (query.contains(Queries.SLOT_NUMBERS_FOR_CARS_WITH_COLOUR.getQuery())) {
			st = query.split(" ");
			String color = st[1];
			if (isParkingLotCreated()) {
				List<Integer> slotNumbers = parkingDataProcessor.fetchSlotNumbersForCarsWithColor(color);
				if (!slotNumbers.isEmpty()) {
					output = OutputGenerator.generateOutputForSlotNumbers(slotNumbers);
				} else {
					output = "No vehicles of color" + " " + color;
				}
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		} else if (query.contains(Queries.SLOT_NUMBER_FOR_REGISTRATION_NUMBER.getQuery())) {
			st = query.split(" ");
			String registrationNumber = st[1];
			if (isParkingLotCreated()) {
				int slotNumber = parkingDataProcessor.fetchSlotNumberForRegistrationNumber(registrationNumber);
				if (slotNumber != -1) {
					output = String.valueOf(slotNumber);
				} else {
					output = "Not found";
				}
			} else {
				output = PARKING_LOT_NOT_CREATED;
			}
			writeOutput(output);
		}
	}
	
	private boolean isParkingLotCreated() {
		if (parkingDataProcessor == null || parkingEntryAndExitProcessor == null) {
			return false;
		}
		return true;
	}
	
	private void writeOutput(String output) {
		outputWriter.writeOutput(output);
	}
	
	static class OutputGenerator {
		
		public static String generateStatus(Ticket tickets[]) {
			String newLineOperator = "\n";
			String lineOne = "Slot No." + "     " + "Registration No" + "            " + "Colour" + newLineOperator; 
			
			for (Ticket t : tickets) {
				if (t != null) {
					String line = t.getSlotNumber() + "            " + t.getRegistrationNumber() + "              " + t.getVehicleColour() + newLineOperator;
					lineOne += line;
				}
			}
			return lineOne;
		}
		
		public static String generateOutputForRegistrationNumbers(List<String> registrationNumbers) {
			String output = "";
			int j = 0;
			for (int i = 0; i < registrationNumbers.size(); i++) {
				output += registrationNumbers.get(i);
				if (j != registrationNumbers.size() - 1) {
					output+= "," + " ";
					j++;
				}
			}
			return output;
		}
		
		public static String generateOutputForSlotNumbers(List<Integer> slotNumbers) {
			String output = "";
			int j = 0;
			for (int i = 0; i < slotNumbers.size(); i++) {
				output += slotNumbers.get(i);
				if (j != slotNumbers.size() - 1) {
					output+= "," + " ";
					j++;
				}
			}
			return output;
		}
	}

	protected void setParkingDataProcessor(ParkingDataProcessor parkingDataProcessor) {
		this.parkingDataProcessor = parkingDataProcessor;
	}

	protected void setParkingEntryAndExitProcessor(ParkingEntryAndExitProcessor parkingEntryAndExitProcessor) {
		this.parkingEntryAndExitProcessor = parkingEntryAndExitProcessor;
	}
}
