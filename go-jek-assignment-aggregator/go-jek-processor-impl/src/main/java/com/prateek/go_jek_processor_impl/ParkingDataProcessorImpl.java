package com.prateek.go_jek_processor_impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.prateek.go_jek_processor_api.ParkingDataProcessor;
import com.prateek.go_jek_processor_api.Ticket;

public class ParkingDataProcessorImpl implements ParkingDataProcessor {

	private Ticket[] slots;
	private TreeSet<Integer> emptySlotsSet;
	private int nearestEmptySlot = 0;
	
	private Map<String, List<Ticket>> colorToVehicleMap;
	private Map<String, Integer> registrationNumberToSlotNumberMap;
	
	public ParkingDataProcessorImpl(int numberOfSlots) {
		this.slots = new Ticket[numberOfSlots];
		this.emptySlotsSet = new TreeSet<Integer>();
		this.colorToVehicleMap = new HashMap<String, List<Ticket>>(); 
		this.registrationNumberToSlotNumberMap = new HashMap<String, Integer>();
	}
	
	private int fetchNearestEmptyParkingSlot() {
		if (emptySlotsSet.isEmpty()) {
			if (nearestEmptySlot == slots.length) {
				return -1;
			}
			return nearestEmptySlot + 1;
		} else {
			int emptySlot = emptySlotsSet.first() + 1;
			emptySlotsSet.remove(emptySlotsSet.first());
			return emptySlot;
		}
	}

	public int vehicleEntryProcessor(String registrationNumber, String vehicleColor) {
		int slotNumber = fetchNearestEmptyParkingSlot();
		if (slotNumber == -1) {
			return slotNumber;
		} else {
			Ticket ticket = new Ticket(registrationNumber, vehicleColor, slotNumber);
			slots[slotNumber - 1] = ticket;
			if (nearestEmptySlot < slots.length) {
				nearestEmptySlot++;
			}
			String color = ticket.getVehicleColour();
			
			if (!registrationNumberToSlotNumberMap.containsKey(registrationNumber)) {
				registrationNumberToSlotNumberMap.put(registrationNumber, slotNumber);
			}
			
			List<Ticket> tickets = colorToVehicleMap.get(color);
			if (tickets != null) {
				tickets.add(ticket);
			} else {
				tickets = new ArrayList<Ticket>();
				tickets.add(ticket);
				colorToVehicleMap.put(color, tickets);
			}
		}
		return slotNumber;
	}

	private void vehicleExitProcessor(Ticket ticket) {
		String registrationNumber = ticket.getRegistrationNumber();
		String vehicleColour = ticket.getVehicleColour();
		
		if (registrationNumberToSlotNumberMap.containsKey(registrationNumber)) {
			registrationNumberToSlotNumberMap.remove(registrationNumber);
		}
		
		List<Ticket> tickets = colorToVehicleMap.get(vehicleColour);
		if (tickets != null) {
			if (tickets.size() >= 2) {
				removeTicket(tickets, ticket);
			} else {
				colorToVehicleMap.remove(vehicleColour);
			}
		}
	}

	private void removeTicket(List<Ticket> tickets, Ticket ticket) {
		for (int i = 0; i < tickets.size(); i++) {
			if (tickets.get(i).equals(ticket)) {
				tickets.remove(i);
				return;
			}
		}
		
	}

	public int deAllocateSlotNumber(int slotNumber) {
		if (slotNumber <= slots.length) {
			Ticket ticket = slots[slotNumber - 1];
			if (ticket != null) {
				slots[slotNumber - 1] = null;
				emptySlotsSet.add(slotNumber - 1);
				
				vehicleExitProcessor(ticket);
			} else {
				return -1;
			}
		} else {
			return -2;
		}
		return slotNumber;
	}

	public Ticket[] fetchCurrentStatus() {
		return slots;
	}

	public List<String> fetchRegistrationNumbersForCarsWithColor(String vehicleColor) {
		List<Ticket> tickets = colorToVehicleMap.get(vehicleColor);
		List<String> registrationNumbers = new ArrayList<String>();
		if (tickets != null && !tickets.isEmpty()) {
			for (Ticket t : tickets) {
				registrationNumbers.add(t.getRegistrationNumber());
			}
		}
		return registrationNumbers;
	}

	public List<Integer> fetchSlotNumbersForCarsWithColor(String vehicleColor) {
		List<Ticket> tickets = colorToVehicleMap.get(vehicleColor);
		List<Integer> slotNumbers = new ArrayList<Integer>();
		if (tickets != null && !tickets.isEmpty()) {
			for (Ticket t : tickets) {
				slotNumbers.add(t.getSlotNumber());
			}
		}
		return slotNumbers;
	}

	public int fetchSlotNumberForRegistrationNumber(String registrationNumber) {
		if (registrationNumberToSlotNumberMap.containsKey(registrationNumber)) {
			return registrationNumberToSlotNumberMap.get(registrationNumber);
		}
		return -1;
	}
	
	protected Ticket[] getSlots() {
		return slots;
	}

	protected Map<String, List<Ticket>> getColorToVehicleMap() {
		return colorToVehicleMap;
	}

	protected Map<String, Integer> getRegistrationNumberToSlotNumberMap() {
		return registrationNumberToSlotNumberMap;
	}

	protected void setNearestEmptySlot(int nearestEmptySlot) {
		this.nearestEmptySlot = nearestEmptySlot;
	}

	protected void setEmptySlotSet(TreeSet<Integer> set) {
		this.emptySlotsSet = set;
	}

	protected TreeSet<Integer> getEmptySlotsSet() {
		return emptySlotsSet;
	}

	protected void setEmptySlotsSet(TreeSet<Integer> emptySlotsSet) {
		this.emptySlotsSet = emptySlotsSet;
	}
	
}
