package com.yourdost.appointment.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yourdost.appointment.dao.SlotRepository;
import com.yourdost.appointment.model.AvailableSlot;
import com.yourdost.appointment.model.RequestData;
import com.yourdost.appointment.model.Response;
import com.yourdost.appointment.model.ResponseData;
import com.yourdost.appointment.model.ResponseStatus;
import com.yourdost.appointment.model.Slot;

@Service
public class AppointmentService {

	@Autowired
	SlotRepository slotRepository;

	public Response blockSlot(RequestData data) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Date date = new Date();
		long currentTime = 0;
		long a = 0;
		long b = 0;
		long startTime = 0;
		long endTime = 0;

		currentTime = sdf.parse(sdf.format(date).toString()).getTime();
		startTime = sdf.parse(data.getStartTime()).getTime();
		endTime = sdf.parse(data.getEndTime()).getTime();

		//get all the block slots for given expert
		List<Slot> slots = slotRepository.findAllByExpertId(data.getExpertId());
		if (endTime < startTime || startTime < currentTime) {
			return this.denySlot(slots);
		}
		if (slots.size() == 0) {
			if (startTime >= currentTime) {
				return this.grantSlot(data);
			} else {
				return this.denySlot(slots);
			}
		}
		else if (slots.size() == 1) {
				a = sdf.parse(slots.get(0).getStartTime()).getTime();
				b = sdf.parse(slots.get(0).getEndTime()).getTime();

			if ((startTime >= b) || (startTime >= currentTime && endTime <= a)) {
				return this.grantSlot(data);
			} else {
				return this.denySlot(slots);
			}
		}

		else {
			this.sortSlots(slots);
			long latestSlotEndTime = sdf.parse(slots.get(slots.size() - 1).getEndTime()).getTime();
			if (startTime >= latestSlotEndTime) {
				return this.grantSlot(data);
			} else {
				for (int i = 1; i < slots.size(); i++) {
						a = sdf.parse(slots.get(i - 1).getEndTime()).getTime();
						b = sdf.parse(slots.get(i).getStartTime()).getTime();

					if (startTime >= a && endTime <= b) {
						return this.grantSlot(data);
					}
				}
			}
			return this.denySlot(slots);
		}
	}

	private Response grantSlot(RequestData data) {

		Response response = new Response();
		ResponseStatus status = new ResponseStatus();
		Slot slot = new Slot();

		slot.setExpertId(data.getExpertId());
		slot.setExpertName(data.getExpertName());
		slot.setUserId(data.getUserId());
		slot.setUserName(data.getUserName());
		slot.setStartTime(data.getStartTime());
		slot.setEndTime(data.getEndTime());
		slot.setType(data.getType());

		slot = slotRepository.save(slot);

		List<Slot> bookedSlot = new ArrayList<Slot>();
		bookedSlot.add(slot);

		status.setStatusCde("00");
		status.setStatusMsg("success");
		status.setStatusDesc("The time slot is blocked successfully ");

		response.setStatus(status);
		return response;
	}

	private Response denySlot(List<Slot> slots) {
		Response response = new Response();
		ResponseStatus status = new ResponseStatus();
		status.setStatusCde("01");
		status.setStatusMsg("Failure");
		status.setStatusDesc("The time slot is not available");

		response.setStatus(status);
		
		List<AvailableSlot> availableSlots = this.getFreeSlots(slots);
		
		ResponseData data = new ResponseData();
		data.setAvailableSlots(availableSlots);
		response.setData(data);
		return response;
	}

	public List<AvailableSlot> getFreeSlots(List<Slot> slots) {
		List<AvailableSlot> availableSlots = new ArrayList<AvailableSlot>();

		for (int i = 0; i < slots.size(); i++) {
			AvailableSlot slot = new AvailableSlot();
			if (i == 0) {
				Date date = new Date();
				System.out.println("date :" + date);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				slot.setStartTime(sdf.format(date));
				slot.setEndTime(slots.get(0).getStartTime());

			} else if (i == slots.size() - 1) {
				slot.setStartTime(slots.get(i).getEndTime());
				slot.setEndTime("");
			} else {
				slot.setStartTime(slots.get(i - 1).getEndTime());
				slot.setEndTime(slots.get(i).getStartTime());
			}
			availableSlots.add(slot);
		}
		return availableSlots;
	}

	private void sortSlots(List<Slot> slots) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		Collections.sort(slots, (s1, s2) -> {
			long p = 0;
			long q = 0;
				try {
					p = sdf.parse(s1.getStartTime()).getTime();
					q = sdf.parse(s2.getStartTime()).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if (p == q) {
				return 0;
			} else if (p > q) {
				return 1;
			} else {
				return -1;
			}
		});
	}
}
