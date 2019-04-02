package com.yourdost.appointment.model;

import java.util.List;

public class ResponseData {
	
	private List<AvailableSlot> availableSlots;

	public List<AvailableSlot> getAvailableSlots() {
		return availableSlots;
	}

	public void setAvailableSlots(List<AvailableSlot> availableSlots) {
		this.availableSlots = availableSlots;
	}
	
}
