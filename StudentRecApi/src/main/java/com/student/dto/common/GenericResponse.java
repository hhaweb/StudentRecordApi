package com.student.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse {

	private String id;
	private boolean status;

	private String message;

	private Object payLoad;
	

	public GenericResponse(String id, boolean status, String message, Object payLoad) {
		super();
		this.id = id;
		this.status = status;
		this.message = message;
		this.payLoad = payLoad;
	}

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
}
