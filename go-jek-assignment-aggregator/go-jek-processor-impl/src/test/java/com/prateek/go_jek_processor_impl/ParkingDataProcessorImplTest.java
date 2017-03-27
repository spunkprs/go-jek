package com.prateek.go_jek_processor_impl;

import org.junit.Before;
import org.junit.Test;

import com.prateek.go_jek_processor_api.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Assert;

public class ParkingDataProcessorImplTest {
	
	private ParkingDataProcessorImpl unit;
	private int parkingSize = 4;
	
	@Before
	public void setUp() {
		unit = new ParkingDataProcessorImpl(parkingSize);
	}
	
	@Test
	public void shouldNotReturnValidParkingNumberWhenTheParkingIsFull() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Blue", 3);
		Ticket t4 = new Ticket("DBAC", "Blue", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		
		updateMaps(slots);
		
		unit.setNearestEmptySlot(slots.length);
		
		Assert.assertEquals(-1, unit.vehicleEntryProcessor("BBBB", "Black"));
		Assert.assertTrue(unit.getEmptySlotsSet().size() == 0);
		Assert.assertFalse(unit.getRegistrationNumberToSlotNumberMap().containsKey("BBBB"));
		Assert.assertFalse(unit.getColorToVehicleMap().containsKey("Black"));
	}
	
	private void updateMaps(Ticket[] slots) {
		Map<String, Integer> mapOne = unit.getRegistrationNumberToSlotNumberMap();
		Map<String, List<Ticket>> mapTwo = unit.getColorToVehicleMap();
		
		for (Ticket t : slots) {
			if (t != null) {
				if (!mapOne.containsKey(t.getRegistrationNumber())) {
					mapOne.put(t.getRegistrationNumber(), t.getSlotNumber());
				}
				
				if (!mapTwo.containsKey(t.getVehicleColour())) {
					List<Ticket> tickets = new ArrayList<Ticket>();
					tickets.add(t);
					mapTwo.put(t.getVehicleColour(), tickets);
				} else {
					List<Ticket> tickets = mapTwo.get(t.getVehicleColour());
					tickets.add(t);
				}
			}
		}
	}

	@Test
	public void shouldReturnValidParkingNumberWhenTheParkingHasSomeSlotsEmptyCaseOne() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t3 = new Ticket("CBAD", "Red", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = null;
		slots[2] = t3;
		slots[3] = t4;
		updateMaps(slots);
		
		unit.setNearestEmptySlot(slots.length);
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(1);
		unit.setEmptySlotSet(set);
		
		int expectedSlotNumber = 2;
		Assert.assertEquals(expectedSlotNumber, unit.vehicleEntryProcessor("BBBB", "Black"));
		Assert.assertEquals(expectedSlotNumber, unit.getRegistrationNumberToSlotNumberMap().get("BBBB").intValue());
		Ticket expectedTicket = new Ticket("BBBB", "Black", expectedSlotNumber);
		Assert.assertNotNull(unit.getSlots()[expectedSlotNumber - 1]);
		Assert.assertEquals(expectedTicket, unit.getSlots()[expectedSlotNumber - 1]);
		Assert.assertEquals(expectedTicket, unit.getColorToVehicleMap().get("Black").get(0));
		Assert.assertEquals(0, unit.getEmptySlotsSet().size());
	}
	
	@Test
	public void shouldReturnValidParkingNumberWhenTheParkingHasSomeSlotsEmptyCaseTwo() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t3 = new Ticket("CBAD", "Red", 3);
		
		slots[0] = t1;
		slots[1] = null;
		slots[2] = t3;
		slots[3] = null;
		updateMaps(slots);
		
		unit.setNearestEmptySlot(slots.length);
		TreeSet<Integer> set = new TreeSet<Integer>();
		set.add(1);
		set.add(3);
		unit.setEmptySlotSet(set);
		
		int expectedSlotNumber = 2;
		Assert.assertEquals(expectedSlotNumber, unit.vehicleEntryProcessor("BBBB", "Black"));
		Assert.assertEquals(expectedSlotNumber, unit.getRegistrationNumberToSlotNumberMap().get("BBBB").intValue());
		Ticket expectedTicket = new Ticket("BBBB", "Black", expectedSlotNumber);
		Assert.assertNotNull(unit.getSlots()[expectedSlotNumber - 1]);
		Assert.assertEquals(expectedTicket, unit.getSlots()[expectedSlotNumber - 1]);
		Assert.assertNull(unit.getSlots()[unit.getSlots().length - 1]);
		Assert.assertEquals(expectedTicket, unit.getColorToVehicleMap().get("Black").get(0));
		Assert.assertEquals(1, unit.getEmptySlotsSet().size());
	}
	
	@Test
	public void shouldReturnValidParkingNumberWhenTheParkingIsEmpty() {
		Ticket[] slots = unit.getSlots();
		updateMaps(slots);
		
		Assert.assertEquals(1, unit.vehicleEntryProcessor("BBBB", "Black"));
		Assert.assertEquals(1, unit.getRegistrationNumberToSlotNumberMap().get("BBBB").intValue());
		Ticket expectedTicket = new Ticket("BBBB", "Black", 1);
		Assert.assertEquals(expectedTicket, unit.getColorToVehicleMap().get("Black").get(0));
	}
	
	@Test
	public void shouldFetchSlotNumberForGivenRegistrationNumber() {
		Ticket[] slots = unit.getSlots();
		
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Black", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		
		updateMaps(slots);
		
		int expectedSlotNumber = 3; 
		
		int actualSlotNumber = unit.fetchSlotNumberForRegistrationNumber("CBAD");
		Assert.assertEquals(expectedSlotNumber, actualSlotNumber);
	}
	
	@Test
	public void shouldFetchRegistrationNumbersForCarsWithGivenColor() {
		Ticket[] slots = unit.getSlots();
		
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Black", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		
		updateMaps(slots);
		
		List<String> expectedRegistrationNumbers = new ArrayList<String>();
		expectedRegistrationNumbers.add("ABCD");
		expectedRegistrationNumbers.add("BCDA");
		
		List<String> actualRegistrationNumbers = unit.fetchRegistrationNumbersForCarsWithColor("Blue");
		Assert.assertEquals(expectedRegistrationNumbers.size(), actualRegistrationNumbers.size());
		for (int i = 0; i < actualRegistrationNumbers.size(); i++) {
			Assert.assertEquals(expectedRegistrationNumbers.get(i), actualRegistrationNumbers.get(i));
		}
	}
	
	@Test
	public void shouldFetchSlotNumbersForCarsWithGivenColor() {
		Ticket[] slots = unit.getSlots();
		
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Black", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		
		updateMaps(slots);
		
		List<Integer> expectedSlotNumbers = new ArrayList<Integer>();
		expectedSlotNumbers.add(1);
		expectedSlotNumbers.add(2);
		
		List<Integer> actualSlotNumbers = unit.fetchSlotNumbersForCarsWithColor("Blue");
		Assert.assertEquals(expectedSlotNumbers.size(), actualSlotNumbers.size());
		for (int i = 0; i < actualSlotNumbers.size(); i++) {
			Assert.assertEquals(expectedSlotNumbers.get(i), actualSlotNumbers.get(i));
		}
	}
	
	@Test
	public void shouldNotDeallocateSlotNumberWhenAskedSlotNumberIsGreaterThanParkingSlotSize() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		
		slots[0] = t1;
		slots[1] = t2;
		updateMaps(slots);
		
		Assert.assertEquals(-2, unit.deAllocateSlotNumber(5));
		Assert.assertEquals(0, unit.getEmptySlotsSet().size());
	}
	
	@Test
	public void shouldNotDeallocateSlotNumberWhenAskedSlotNumberDoesNotHaveAnyVehicleMapped() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		
		slots[0] = t1;
		updateMaps(slots);
		
		Assert.assertEquals(-1, unit.deAllocateSlotNumber(3));
		Assert.assertEquals(0, unit.getEmptySlotsSet().size());
	}
	
	@Test
	public void shouldDeallocateSlotNumber() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Black", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		updateMaps(slots);
		
		Assert.assertEquals(3, unit.deAllocateSlotNumber(3));
		Assert.assertEquals(1, unit.getEmptySlotsSet().size());
		Assert.assertEquals(2, unit.getEmptySlotsSet().first().intValue());
		Assert.assertNull(unit.getSlots()[unit.getEmptySlotsSet().first().intValue()]);
	}
	
	@Test
	public void shouldFetchCurrentStatus() {
		Ticket[] slots = unit.getSlots();
		Ticket t1 = new Ticket("ABCD", "Blue", 1);
		Ticket t2 = new Ticket("BCDA", "Blue", 2);
		Ticket t3 = new Ticket("CBAD", "Black", 3);
		Ticket t4 = new Ticket("DBAC", "White", 4);
		
		slots[0] = t1;
		slots[1] = t2;
		slots[2] = t3;
		slots[3] = t4;
		updateMaps(slots);
		
		Ticket[] replicaSlots = createReplica(slots);
		Ticket[] actualSlots = unit.fetchCurrentStatus();
		Assert.assertEquals(replicaSlots.length, actualSlots.length);
		verifyStatus(replicaSlots, actualSlots);
	}

	private void verifyStatus(Ticket[] replicaSlots, Ticket[] slots) {
		for (int i = 0; i < replicaSlots.length; i++) {
			Assert.assertEquals(replicaSlots[i].getRegistrationNumber(), slots[i].getRegistrationNumber());
			Assert.assertEquals(replicaSlots[i].getVehicleColour(), slots[i].getVehicleColour());
			Assert.assertEquals(replicaSlots[i].getSlotNumber(), slots[i].getSlotNumber());
		}
		
	}

	private Ticket[] createReplica(Ticket[] slots) {
		Ticket[] replicaSlots = new Ticket[slots.length];
		for (int i = 0; i < slots.length; i++) {
			replicaSlots[i] = new Ticket(slots[i].getRegistrationNumber(), slots[i].getVehicleColour(), slots[i].getSlotNumber());
		}
		return replicaSlots;
	}
}
