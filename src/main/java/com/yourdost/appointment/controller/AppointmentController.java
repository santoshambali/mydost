package com.yourdost.appointment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yourdost.appointment.dao.SlotRepository;
import com.yourdost.appointment.model.Request;
import com.yourdost.appointment.model.Response;
import com.yourdost.appointment.model.ResponseStatus;
import com.yourdost.appointment.service.AppointmentService;

@RestController
public class AppointmentController {

	@Autowired
	SlotRepository slotRepository;
	
	@Autowired
	AppointmentService appointmentService;
	
	@RequestMapping(method=RequestMethod.POST, value = "/blockSlot")
	public Response bookAppointment(@RequestBody Request request) {

		try {
			return appointmentService.blockSlot(request.getData());
		}
		catch(Exception e) {
			Response response = new Response();
			ResponseStatus status = new ResponseStatus();
			status.setStatusCde("01");
			status.setStatusMsg("Failure");
			status.setStatusDesc("Please give proper inputs, Example date format : dd-MM-yyyy hh:mm:ss");
			
			response.setStatus(status);
			
			return response;
		}
	}
}
