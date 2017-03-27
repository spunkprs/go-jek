package com.prateek.go_jek_processor_api;

public class Ticket {
	
	String registrationNumber;
	String vehicleColour;
	int slotNumber;
	
	public Ticket(String registrationNumber, String vehicleColour, int slotNumber) {
		this.registrationNumber = registrationNumber;
		this.vehicleColour = vehicleColour;
		this.slotNumber = slotNumber;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public String getVehicleColour() {
		return vehicleColour;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
		result = prime * result + slotNumber;
		result = prime * result + ((vehicleColour == null) ? 0 : vehicleColour.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (registrationNumber == null) {
			if (other.registrationNumber != null)
				return false;
		} else if (!registrationNumber.equals(other.registrationNumber))
			return false;
		if (slotNumber != other.slotNumber)
			return false;
		if (vehicleColour == null) {
			if (other.vehicleColour != null)
				return false;
		} else if (!vehicleColour.equals(other.vehicleColour))
			return false;
		return true;
	}
	
}
