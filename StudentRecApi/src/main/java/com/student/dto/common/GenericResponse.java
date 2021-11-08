package com.student.dto.common;

public class GenericResponse {


	private boolean status;

	private String message;

	private Object payLoad;

	public GenericResponse(boolean status, String message, Object payLoad) {
		this.status = status;
		this.message = message;
		this.payLoad = payLoad;
	}

	public GenericResponse(boolean status, String string) {

		this.status = status;
		this.message = string;
	}

	public GenericResponse(boolean status, Object payLoad) {

		this.status = status;
		this.payLoad = payLoad;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getPayLoad() {
		return payLoad;
	}

	public void setPayLoad(Object payLoad) {
		this.payLoad = payLoad;
	}
	
	

}
