package com.yourdost.appointment.model;

public class Response {

	private ResponseStatus status;
	private ResponseData data;
	
	public ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	public ResponseData getData() {
		return data;
	}
	public void setData(ResponseData data) {
		this.data = data;
	}
	
	
}
